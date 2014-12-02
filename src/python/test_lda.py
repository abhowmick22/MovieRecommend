#!/bin/usr/env python

import numpy as np
import lda
from sklearn.feature_extraction.text import TfidfVectorizer
import lda.datasets

# use our movie summaries 
f = open('plot_summaries.txt', 'r')

data = []

#for first 100 reviews
for i in range(0,100):
	line = f.readline().split('\t',1)[1]
	data.append(line)	
		
# Document-term matrix
vectorizer = TfidfVectorizer()
#X = vectorizer.fit_transform(data)
X = lda.datasets.load_reuters()
#print("type(X): {}".format(type(X)))
#print("shape: {}\n".format(X.shape))

# get the vocab
#g = open('../../data/cleanedVocab_nostemming_allwords.txt', 'r')
#lines = g.readlines()
#vocab = [word.split(',')[1].rstrip('\n') for word in lines]
vocab = lda.datasets.load_reuters_vocab()
#vocab = np.asarray(vectorizer.get_feature_names())
#print("type(vocab): {}".format(type(vocab)))
#print("len(vocab): {}\n".format(len(vocab)))
#print np.array(vocab)

model = lda.LDA(n_topics=20, n_iter=5000, random_state=1)
model.fit(X)
topic_word = model.components_
n_top_words = 10
for i, topic_dist in enumerate(topic_word):
	topic_words = np.array(vocab)[np.argsort(topic_dist)][:-n_top_words:-1]
	print('Topic {}: {}'.format(i, ' '.join(topic_words)))

