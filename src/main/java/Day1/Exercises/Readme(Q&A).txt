Question 1 - Explain what you see in exercise 1)
    I see that once the site is loaded it gets cached, and the loading time is faster.
    But when I change the file name it isn't cached anymore, so it loads slower the first time.

Question 2 - Purpose of the Connection: header)
    You can keep the connection alive so you don't have to establish it again.
    You can also keep it alive within a time limit or just close it.

Question 3 - Explain the first two requests)
    I requested to go to http://studypoints.dk: but ended up at https://studypoints.dk/#/view1.
    Second request went to bootstrap cdn at https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css,
    where the css used comes from.

Question 4 - POST method & get)
    With the GET method the parameters are written in the url, and in the post method they are written in the body.

Question 5 - Server maintain state between susequent calls)
    It's because we make a session cookie that contains the name. The session cookie is only alive as long as the
    browser is still open (not including the tab with the site).
    To see the picture, open the SessionDemo servlet!

Question 6 - Persistent cookies)
    "Cookies store a set of user specific information, such as a reference identifier for a database record that holds customer information. 
    The Web server embeds the cookie into a user's Web browser so that the user's information becomes available to other pages within the site; users do not have to reenter their information for every page they visit. 
    Cookies are a good way to gather customer information for Web-based shopping, for retaining the personal preferences of the Web user, or for maintaining state about the user." - from a google search

    We use persistent cookies because they are time based and can therefore be stored even when the browser is closed.
    