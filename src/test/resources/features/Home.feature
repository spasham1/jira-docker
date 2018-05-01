@Search
Feature: user enters text in search box and hints are displayed

  Scenario Outline: user navigates to costa web page
    Given I'm on costa web page
    When I hover on tab <tab>
    Then I get popup with further options
    Examples:
    |tab|
    |Locations|
    |Coffee Club|
    |Responsibility|

  Scenario Outline: : user enters text in search box

    Given I hover on tab <tab>
    When I enter text in search box <search_text>
    And I click go button
    Then search results are displayed

    Examples:
    | tab | search_text |
    |Locations | holborn |