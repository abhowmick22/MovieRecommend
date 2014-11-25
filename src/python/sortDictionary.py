# Script to sort the vocabulary; can be used to add additional rules later.
import sys;

# Reading processed summaries and vocabulary 
vocabFile = open('../../data/vocab_nostemming.txt', 'r');

# Checking for existance of files
if(vocabFile == None):
    sys.exit('Vocabulary file not found\n');
   
# Reading vocabulary files
# Stripping \n sign
vocabLines = [s.rstrip("\n\r") for s in vocabFile.readlines()]

# Closing files
vocabFile.close();

# Sorting the vocabulary
# Extracting words and sorting them
words = [];
for i in vocabLines:
    splits = i.split(',');
    # Weed out words that dont start with an alphabet
    if(ord(splits[1][0]) > 96 and ord(splits[1][0]) < 123):
        words.append(splits[1]);

#sys.exit();
words.sort()

# Dumping the new dictionary
majorWords = ['one', 'film', 'back', 'two'];
newDictFile = open('../../data/sortedVocab_majorremoved.txt', 'w');
wordId = 0;
for i in xrange(0, len(words)):
    if(words[i] in majorWords):
        continue;

    # Adding a new word in the dictionary
    newLine = str(wordId) + ',' + words[i] + '\n';
    wordId = wordId + 1;
    newDictFile.write(newLine)
    
newDictFile.close();
