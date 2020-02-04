# WeatherApp

This app is developed in Kotlin with MVVM architecture by using components are :

- Dagger
- Live data
- View model
- Rx java
- Retrofit

Testing framework:

- Junit
- Espresso

Stetho library - for debugging API request/response and DB

As per the acceptance criteria i have covered all the steps :

  step 1 :  Taking list from UI with multiple cities and calling APIs in parallel then feeding the response to UI
  step 2 :  Part A: Fetching current city name using current lat long API
            Part B: Fetching 5 days and 3 hours each w.t.r city name

Error Handling :
       - Handled basic UI error handling w.r.t requirement
       - Handled basic generic error message for ViewModel exception

Pending things :
       - Didn't handled all types of exception with specific list item
       - Didn't handle idling resource for espresso test cases during network calls

