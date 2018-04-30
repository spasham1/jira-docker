@Search
Feature: user enters text in search box and hints are displayed

  Scenario Outline: user navigates to costa web page
    Given I'm on costa web page
    When I hover on tab <tab>
    Then I get popup with further options
    Examples:
    |tab|
    |Locations|

  Scenario Outline: : user enters text in search box

    Given search box exists
    When I enter text in search box <search_text>
    Then search results are displayed
    
    When I select search suggestion link <select_link>
    Then I can see search results on page <search_result>
    Examples:
    |search_text| select_link | search_result |
    |SMD291AX250T3 | 1 | SMD291AX250T3 |
    |soldering | 3 | Soldering Iron Tips |

  Scenario: search for a part number and checkout

    Given page contains search results for 01508
    When I add product to basket
    And I click checkout basket and confirm
    Then I get login popup

  Scenario: : user sets filter

    Given page contains search results for Soldering Irons
    When I set filter Brand with value Portasol
    Then I can see product Portasol Gas Soldering Iron, 60W with part_number 10181060
    And product count is 3

    When I set filter Wattage with value 125W
    Then I can see product Portasol Gas Soldering Iron, 125W with part_number 11180070
    And product count is 1

    #user checks out
    When I add product Portasol Gas Soldering Iron, 125W with quantity 1 to basket
    And I click checkout basket and confirm
    Then I get login popup
