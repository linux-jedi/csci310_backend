Feature: Search Page
        Scenario: Text Input
                Given I am on search page
                When I click on the text box
                Then it should display prompt text "Enter food"
        Scenario: Numeric Input
                Given I am on the search Page
                When I hover over the integer box
                Then it should display hover text "Number of items to show in results"
        Scenario: Search
                Given I am on the search Page
                When I click the "Feed Me" button
                Then it should begin searching for what I had placed in the text boxes