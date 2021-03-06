# Mobile App
## MVP

- [x] The UI should have one month hard coded view
- [x] Ignore user/login, just have one hardcoded user
- [x] Click on a day box and be able to create a new event on that day which get sent to the back end on clicking submit
    - [x] The form should have start time, end time, description, and submit
    - [x] Once submit is clicked the form should dissapear
    - [x] Event should now appear on that da's box
    - [x] Events cannot span multiple days. Must start and end the same day
- [x] Show all events the user has on their calendar
- [x] the UI should have 4 rows 7 boxes
- [x] The application should communicate with an API backend using JSON

* Refresh app as needed. (Slow response due to heroku intial response time, shuts down after lack of usage)
  * *Potentially resolved* Set a reoccuring pinger to keep the website running.

## Bonus
- [x] Switch between months
- [x] Day View
- [ ] Week View
- [ ] Handle events spanning multiple days
- [x] Handle too many events to fit your box UI on given day
- [x] You should be able to update/delete events.
- [x] The UI should have 5 rows of 7 boxes with correct date on the correct days

# Back End

## MVP
- [x] POST /events, creates an event
    
- [x] GET /events, return all events

## Bonus
- [x] DELETE /events/:id, deletes an event
- [x] PUT/events/:id, updates an existing event

# Monthly View
* <img src="https://github.com/Jzheng213/SpotifyCalendarAndroid/blob/master/monthly.png" width="400">

# Daily View
* <img src="https://github.com/Jzheng213/SpotifyCalendarAndroid/blob/master/Daily.png" width="400">

# Show Page
* <img src="https://github.com/Jzheng213/SpotifyCalendarAndroid/blob/master/Show.png" width="400">

# TODO
- [ ] Deploy to google store
- [ ] Handle events spanning multiple days
- [ ] Sort daily items in chronological order
- [ ] Have a local cache to save events for offline usage
- [ ] Refactor: use android replace string with android string resources
- [ ] Refactor: code modularize & follow OOP principles
- [ ] Clean up any code smells
