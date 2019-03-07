Feature: List Management page
        Scenario: Clicking on an Item
                Given I am on the List Management page
                When I click on an item
                Then It should redirect me to the page associated with that item
        Scenario: Adding Item to Another List
                Given I have selected a List from the dropdown menu
                When I click on that lists
                The item will be moved to that list
        Scenario: Return to Results page
                Given I am on the List Management page
                When I click on a Return to Results Page button
                Then it should take me to the Results Page
        Scenario: Return to Search
                Given I am on the List Management page
                When I click on a Return to Search Page button
                Then it should take me to the Search Page
        Scenario: Dropdown box for Predefined Lists
                Given I am on the list Management Page
                When I click on Manage List
                Then there will be a drop down box displayed for different lists
