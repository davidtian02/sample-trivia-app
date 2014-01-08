sample-trivia-app
=================

This is for a trivia game. some parts are hidden on purpose. this is just to show more of structure of code.

The trivia game contains a bunch of words read from the res/raw folder, is parsed through DataController.java, and fed through to `TermsDictionary`, which happens to have a bunch of `Term`s. then, when game starts, the `GameActivity` takes care of the game logic. 
	
The app is written in a MVC structure. there were a few things that could have been written out better - putting a list of buttons in a group, or at least store them more dynamically than hardcoding in a couple of places. the music is something that could have been moved out to a Service, now looking back.

The entire app was created around Oct 2012, and took around two months to finish, starting from scratch.

