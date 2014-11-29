# function to sort the vocabulary; can be used to add additional rules later.
import sys;

def sortAndDumpDictionary(dictionary, newDictionaryPath):
    # Removing words that dont start with a lower case alphabet
    newDictionary = [];
    for word in dictionary.keys():
        # Weed out words that dont start with an alphabet
        if(ord(word[0]) > 96 and ord(word[0]) < 123):
            newDictionary.append(word);

    # Sorting the words
    newDictionary.sort()

    # Dumping the new dictionary, other necessary count based filters can be implemented here
    majorWords = ['one', 'film', 'back', 'two'];
    newDictFile = open(newDictionaryPath, 'w');
    wordId = 0;
    for i in xrange(0, len(newDictionary)):
        if(words[i] in majorWords):
            continue;

        # Adding a new word in the dictionary
        newLine = str(wordId) + ',' + words[i] + '\n';
        wordId = wordId + 1;
        newDictFile.write(newLine)
        
    newDictFile.close();
    return 
