#!/bin/usr/env python

from pylab import *
#import matplotlib.pyplot as plt
from sklearn.datasets import fetch_20newsgroups
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.manifold import TSNE
from sklearn.decomposition import PCA
from sklearn.decomposition import TruncatedSVD

import math

import pylab
import matplotlib


class AnnoteFinder:
  """
  callback for matplotlib to display an annotation when points are clicked on.  The
  point which is closest to the click and within xtol and ytol is identified.
    
  Register this function like this:
    
  scatter(xdata, ydata)
  af = AnnoteFinder(xdata, ydata, annotes)
  connect('button_press_event', af)
  """

  def __init__(self, xdata, ydata, annotes, axis=None, xtol=None, ytol=None):
    self.data = zip(xdata, ydata, annotes)
    if xtol is None:
      xtol = ((max(xdata) - min(xdata))/float(len(xdata)))/2
    if ytol is None:
      ytol = ((max(ydata) - min(ydata))/float(len(ydata)))/2
    self.xtol = xtol
    self.ytol = ytol
    if axis is None:
      self.axis = pylab.gca()
    else:
      self.axis= axis
    self.drawnAnnotations = {}
    self.links = []

  def distance(self, x1, x2, y1, y2):
    """
    return the distance between two points
    """
    return math.hypot(x1 - x2, y1 - y2)

  def __call__(self, event):
    if event.inaxes:
      clickX = event.xdata
      clickY = event.ydata
      if self.axis is None or self.axis==event.inaxes:
        annotes = []
        for x,y,a in self.data:
          if  clickX-self.xtol < x < clickX+self.xtol and  clickY-self.ytol < y < clickY+self.ytol :
            annotes.append((self.distance(x,clickX,y,clickY),x,y, a) )
        if annotes:
          annotes.sort()
          distance, x, y, annote = annotes[0]
          self.drawAnnote(event.inaxes, x, y, annote)
          for l in self.links:
            l.drawSpecificAnnote(annote)

  def drawAnnote(self, axis, x, y, annote):
    """
    Draw the annotation on the plot
    """
    if (x,y) in self.drawnAnnotations:
      markers = self.drawnAnnotations[(x,y)]
      for m in markers:
        m.set_visible(not m.get_visible())
      self.axis.figure.canvas.draw()
    else:
      t = axis.text(x,y, "(%3.2f, %3.2f) - %s"%(x,y,annote), )
      m = axis.scatter([x],[y], marker='d', c='r', zorder=100)
      self.drawnAnnotations[(x,y)] =(t,m)
      self.axis.figure.canvas.draw()

  def drawSpecificAnnote(self, annote):
    annotesToDraw = [(x,y,a) for x,y,a in self.data if a==annote]
    for x,y,a in annotesToDraw:
      self.drawAnnote(self.axis, x, y, a)



#help(TSNE)
#categories = ['alt.atheism', 'talk.religion.misc']
#newsgroups = fetch_20newsgroups(subset="train", categories=categories)
#print(len(newsgroups.data))
# newsgroups.data is a list of 857 raw texts

# use our movie summaries instead of newsgroups.data
f = open('plot_summaries.txt', 'r');
data = []
movies = []
genres = []
labels = []
colors = []		# one of 9 colors in range(9)

#for line in f:
for i in range(0,1000):
	line = f.readline()
	movieId = line.split('\t',1)[0]
	line = line.split('\t',1)[1]
	data.append(line)	
	matches = [ line for line in open('movie.metadata.tsv') if movieId in line]
	# if moviename doesn't exist
	if not matches:
		movieName = "Unknown"
		genre = "xxxxxxxxxxxxxxxxxxxxxxx"
	else:	
		movieName = matches[0].split('\t')[2]
		genre = matches[0].split('\t')[8]
		
	# determine color of genre
	c = 0				# default is others category
	if "Mystery" in genre or "Thriller" in genre:
		c = 1
		genre = "Mystery/Thriller"
	elif "Romantic" in genre or "Romance" in genre:
		c = 2
		genre = "Romantic"
	elif "Drama" in genre:
		c = 3
		genre = "Drama"
	elif "Comedy" in genre:
		c = 4
		genre = "Comedy"
	elif "Horror" in genre:
		c = 5
		genre = "Horror"
	elif "Musical" in genre:
		c = 6
		genre = "Musical"
	elif "Crime" in genre:
		c = 7
		genre = "Crime"
	elif "Documentary" in genre:
		c = 8
		genre = "Documentary"
	elif "Action" in genre or "Adventure" in genre:
		c = 9
		genre = "Action/Adventure"
	else:
		genre = "Other"

	colors.append(c)
	movieName = movieName.decode('unicode-escape')
	genre = genre.decode('unicode-escape')
	movies.append(movieName)
	genres.append(genre)
	labels.append(movieName + "\n" + genre)

#labels = movies
#print len(data)
#s = movies[0]
#data = list(f)
'''
if isinstance(s, str):
	print "ordinary string"
elif isinstance(s, unicode):
	print "unicode string"
else:
	print "not a string"
'''
vectors = TfidfVectorizer().fit_transform(data)

#print(size(newsgroups.target))
#print(len(newsgroups.target))
#print(newsgroups.filenames)
#print(repr(vectors))
#print(vectors);


X_reduced = TruncatedSVD(n_components=50, random_state=0).fit_transform(vectors)
X_embedded = TSNE(n_components=2, perplexity=30, verbose=2).fit_transform(X_reduced)


fig = figure(figsize=(10, 10))
ax = axes(frameon=False)
setp(ax, xticks=(), yticks=())
subplots_adjust(left=0.0, bottom=0.0, right=1.0, top=0.9,
                wspace=0.0, hspace=0.0)
#scatter(X_embedded[:, 0], X_embedded[:, 1], c=newsgroups.target, marker="x")
scatter(X_embedded[:, 0], X_embedded[:, 1], c=colors, marker="x")
print(labels)
af =  AnnoteFinder(X_embedded[:, 0], X_embedded[:, 1], labels)
connect('button_press_event', af)
'''
for label, x, y in zip(labels, X_embedded[:, 0], X_embedded[:, 1]):
    plt.annotate(
        label, 
        xy = (x, y), xytext = (-20, 20),
        textcoords = 'offset points', ha = 'right', va = 'bottom',
        bbox = dict(boxstyle = 'round,pad=0.5', fc = 'yellow', alpha = 0.5),
        arrowprops = dict(arrowstyle = '->', connectionstyle = 'arc3,rad=0'))
'''
show()

