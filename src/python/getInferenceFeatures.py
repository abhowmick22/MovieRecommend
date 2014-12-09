#Script to get summaries for inference
# Steps:
# Read file from UP, map file, summaries file
# For each movie, get correponding id for summaries, read that summary and dump it in the file

import re;

ratingFile = open('../../data/movies.dat', 'r');
mapFile = open('../../data/IDmap.csv', 'r');
summaryFile = open('../../data/nostemming/processed_summaries.txt', 'r');

ratingLines = [s.rstrip("\n\r") for s in ratingFile.readlines()]
mapLines = [s.rstrip("\n\r") for s in mapFile.readlines()]
summaryLines = [s.rstrip("\n\r") for s in summaryFile.readlines()]

ratingLines = [i.split("::") for i in ratingLines];
mapLines = [i.split(",") for i in mapLines];
#summaryLines = [i.split(" ") for i in summaryLines];


# Processing the map file
mapping = {};
for movie in mapLines:
    mapping[ int(movie[1]) ] = int(movie[0]);

# Processing the summary files
summaries = {};
iterId = 0;
# Processing the summary files
for line in summaryLines:
    feature = '';

    lineSplit = line.split(',');
    movieId = int(lineSplit[0]);

    # Debug message
    iterId = iterId + 1;
    if(iterId % 100 == 0):
        print iterId

    # Reading the set of words using regular expressions
    string = re.findall(r'\[([^]]*)\]', line);
    string = re.findall('\'([^\']*)\'', string[0]);

    for i in string:
        if(len(i) < 1):
            continue;

        # Checking for each letter to be alphabet
        considerWord = True;

        for letter in list(i):
            # If any of the word is not alphabet, ignore the word
            if(ord(letter) <= 96 or ord(letter) >= 123):
                considerWord = False;
                break;

        # Continue with next word if not to be considered
        if(considerWord == False):
            continue;
        
        elif(feature == ''):
            feature = i;

        else:
            feature = feature + ' ' + i; 

    # Dumping the feature vector into the file, only if number of features using the curent dictionary is greater than 1
    if(len(feature) > 0):
        summaries[movieId] = feature;

movieLensIdFile = open('../../data/gibbs/movieLensId.txt', 'wb');
inferenceFile = open('../../data/gibbs/inferencefeatures.txt', 'wb');
trainingFile = open('../../data/gibbs/trainingfeatures.txt', 'wb');

# For each movie in the ratings, check for corresponding id in 
movieCount = 0;
infMovieSummaries = []; # Summaries of movies for which inference is possible
infMovieIds = []; # Ids wrt MovieLens for which inference is possible
for movie in ratingLines:
    mLensId = int(movie[0]);

    # Check if present in mapping
    if(mLensId in mapping.keys()):
        # Check if present in summaries
        if(mapping[mLensId] in summaries.keys()):
            movieCount = movieCount + 1;
            infMovieIds.append(mLensId);
            infMovieSummaries.append(summaries[mapping[mLensId]]);
    
#Writing the inference summaries and movie ids
inferenceFile.write(str(movieCount) + '\n');
for i in xrange(0, len(infMovieIds)):
    movieLensIdFile.write(str(infMovieIds[i]) + '\n');
    inferenceFile.write(infMovieSummaries[i] + '\n');

print 'Wrote %d movies for summary inference' % movieCount;

# Writing the remaining files for training LDA
trainCount = 0;
trainSummaries = [];
for i in summaries.keys():
    if(i not in mapping.values()):
        trainSummaries.append(summaries[i]);
        trainCount = trainCount + 1;
    
# writing training files back
trainingFile.write(str(trainCount) + '\n');
for i in trainSummaries:
    trainingFile.write(i + '\n');

print 'Wrote %d movies for summary LDA training' % trainCount;
# Closing files
ratingFile.close();
mapFile.close();
summaryFile.close();
movieLensIdFile.close();
inferenceFile.close();
trainingFile.close();
