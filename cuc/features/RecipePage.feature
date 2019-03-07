Feature: Recipe Page
        Scenario: Button to trigger printable version
                Given I am on the Recipe Page
                When I click on the Print button
                Then it should toggle the browser's print wizard
        Scenario: Return to Results page
                Given I am on the Recipe page
                When I click on a Return to Results Page button
                Then it should take me to the Results Page
        Scenario: Dropdown box for Predefined Lists
                Given I am on the Recipe Page
                When I click on Manage List
                Then there will be a drop down box displayed for different lists
        Scenario: Add to List
                Given I selected a List on the dropdown box
                When I click on the Add to List
                Then the Recipe should be added to that respective list
