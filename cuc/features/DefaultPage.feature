Feature: Default Page
        The default page is the search page.
        Scenario: DefaultSearchPage
                Given the user navigates to IM HUNGRY
                When the user hits enter on his keyboard
                Then the 'Search Page' should be displayed