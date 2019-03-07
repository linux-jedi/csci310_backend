Feature: Results Page
        Scenario: Display Collage of user search
                Given I searched for an item on the search page
                When the results page loads
                Then it should display a collage of my searched item
        Scenario: Display Title of user search
                Given I searched for an item on the search Page
                When the result page loads
                Then it should display a title of the form "Results for <Item>"
        Scenario: No Results found
                Given I searched for an unknown item on the search page
                When the result page loads
                Then there should be no collage, but there should be a title of form "Results for <Item>"
        Scenario: List Management Button
                Given the user is on the Results page
                When I click on the Manage List button
                Then there will be a drop down box displayed for different lists
        Scenario: List Management Drop down
                Given the user clicked on the Manage List Button
                When I click on any of the lists on the drop down
                Then it should redirect me to the List Manage Page associated with that list
        Scenario: Restaurant List
                Given I have searched for a known item
                When I am on the Search page
                Then I should see a list of restaurants on the left associated with that item
        Scenario: Recipe List
                Given I have searched for a known item
                When I am on the Search page
                Then I should see a list of recipes on the right associated with that item
        Scenario: Clicking On Restaurant
                Given I am on the search page
                When I click on a restaurant on the restaurant list
                Then it should take me to the Restaurant Page
        Scenario: Clicking on Recipe
                Given I am on the search page
                When I click on a recipe on the recipe list
                Then it should take me to the Recipe Page
        Scenario: Return to Search
                Given I am on the search page
                When I click on a Return to Search Page button
                Then it should take me to the Search Page
