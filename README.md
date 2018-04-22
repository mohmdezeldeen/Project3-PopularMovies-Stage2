# Project3-PopularMovies-Stage2

## Project Overview
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages. First, let's talk about stage 1.
In this stage 1: you’ll build the core experience of your movies app.
You app will:
* Present the user with a grid arrangement of movie posters upon launch.
* Allow your user to change sort order via a setting:
  * The sort order can be by most popular or by highest-rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
  * original title
  * movie poster image thumbnail
  * A plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date
  
In this stage 2: you’ll add additional functionality to the app you built in Stage 1.

* You’ll add more information to your movie details view:
  * You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
  * You’ll allow users to read reviews of a selected movie.
  * You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
  * You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.



## Why this Project

To become an Android developer, you must know how to bring particular mobile experiences to life. Specifically, you need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. You will hone these fundamental skills in this project.
By building this app, you will demonstrate your understanding of the foundational elements of programming for Android. Your app will communicate with the Internet and provide a responsive and delightful user experience.

## What Will I Learn After Stage 1?
Through this project, you will:
- Learn how to submit projects for review
- Practice JSON parsing to a model object
- Design an activity layout
- Populate all fields in the layout accordingly

## How Do I Complete this Project?
- You will fetch data from the Internet with theMovieDB API.
- You will use adapters and custom list layouts to populate list views.
- You will incorporate libraries to simplify the amount of code you need to write

## Required Tasks
- Build a UI layout for multiple Activities.
- Launch these Activities via Intent.
- Fetch data from themovieDB API

## Libraries
- ButterKnife
- Picasso
- Retrofit


## Important 
* I've removed my api key from code, so i can push it in public GitHub, So you should replace it with your api key.
  * replace in **gradle.properties** file **API_KEY="YOUR_API_KEY_HERE"**
## Some Images
![screenshot_1](https://user-images.githubusercontent.com/20706577/39101027-359b2cd4-4694-11e8-993d-5810965df115.png)

![screenshot_2](https://user-images.githubusercontent.com/20706577/39101029-40137ed2-4694-11e8-9f49-5bc37fd69690.png)

![screenshot_3](https://user-images.githubusercontent.com/20706577/39101030-41d9d7de-4694-11e8-8981-149740316c39.png)


