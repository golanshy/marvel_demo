# marvel_demo
This demo app is using the [marvel comics apis](https://developer.marvel.com/) to pull records. It also allows the user to search for a comic by title.
</br>
<img src="/device-2020-01-02-121650.png" width="400">
<img src="/device-2020-01-02-122938.png" width="400">

## architecture
The app is built in a modular way, see the architecture diagram below:
app module contains the Application class and injection
feature-marvel module contains the code for the marvel search functionality.
<img src="/architecture.png" width="600">

## design pattern
MVVM using Android ViewModel architecture component with state management (LCE Loading, Content, Error) 





