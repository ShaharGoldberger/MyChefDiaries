My Chef Diaries App
The functionality of the application :
1.	User profile management - changing the user's name and uploading a new photo (from the gallery or taking a live photo).
2.	Connecting to the application.
3.	Disconnecting from the application.
4.	Uploading a recipe to the application (recipe name, preparation time, ingredients, category). When uploading a recipe, there is an option to upload a picture of the recipe - a picture from the gallery or a picture from the device (or without a picture and then the icon of the application appears next to the dish).
5.	Recipe search by category or by free text.
6.	Adding a recipe to favorites - the user has the option to mark a recipe he liked by clicking on the heart icon. The user also has the option to remove a recipe from the list of favorites.
7.	Shopping cart management - a user connected to the application can save a shopping cart in the application - add groceries and download groceries.
8.	All the recipes, users and other data are saved in a Database handled by Firebase.

The app has 3 fragments - feed, preferred shopping cart and profile.
The feed displays all the recipes the user has created and an option to switch to additional fragments found in the bar. In addition, 3 buttons are displayed to which the user can go - upload a recipe, search for a recipe by category and search for a recipe by free text.
In favorites - all the recipes created by the user are displayed. Clicking on a recipe that the user created will lead the user to the screen where the recipe will appear.
In the profile - the user has the option to change his picture (from the device or from the gallery), change his name, and exit the application (by clicking the logout button).
In a shopping cart - the user has the option to add components and remove components from the cart.
