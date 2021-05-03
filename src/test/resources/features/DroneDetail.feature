Feature: Retrieve list of drones as well as details of individual drone.

  @T_001
  Scenario: Verify that list of drones is retrieved.
    Given at least one drone is present
    When list of drones is fetched
    Then drones list is displayed

  @T_002
  Scenario Outline: Verify that details of a drone are retrieved.
    Given a drone with id <id> is present
    When drone details are fetched
    Then details of drone are displayed
    Examples:
      | id |
      | 2  |

  @T_006
  Scenario: Verify that empty drone list results in error.
    Given no drone is present
    When list of drones is fetched
    Then drone list is not found

  @T_007
  Scenario Outline: Verify that invalid id while retrieving drone details results in error.
    Given a drone with id <id> is not present
    When drone details are fetched
    Then details of drone are not found
    Examples:
      | id |
      | -1 |
