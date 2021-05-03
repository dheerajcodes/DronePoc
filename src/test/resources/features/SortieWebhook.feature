Feature: Webhook for the drone to update its sortie details.

  @T_005
  Scenario Outline: Verify that drone can update sortie details.
    Given a drone with id <drone id> is present
    And drone is executing instruction with id <instruction id>
    When current location of drone is <current location>
    And  current speed of drone is <current speed>
    And status of drone is <status>
    And estimated time of arrival is <estimated time>
    And warehouse location is <warehouse location>
    And destination location is <destination location>
    And drone sends sortie update request to webhook
    Then sortie details are updated successfully
    Examples:
      | drone id | instruction id | current location | destination location | warehouse location | status      | estimated time | current speed |
      | 5        | 1              | 30.21,21.02      | 40.25,30.54          | 30.21,22.32        | in_progress | 20.45          | 45.50         |

  @T_018
  Scenario Outline: Verify that invalid drone id while updating sortie details results in error.
    Given a drone with id <drone id> is not present
    And instruction with id <instruction id> is present
    When current location of drone is <current location>
    And  current speed of drone is <current speed>
    And status of drone is <status>
    And estimated time of arrival is <estimated time>
    And warehouse location is <warehouse location>
    And destination location is <destination location>
    And drone sends sortie update request to webhook
    Then sortie details fails to update with error <error>
    Examples:
      | drone id | instruction id | current location | destination location | warehouse location | status      | estimated time | current speed | error           |
      | -1       | 1              | 30.21,21.02      | 40.25,30.54          | 30.21,22.32        | in_progress | 20.45          | 45.50         | DroneIdNotFound |

  @T_019
  Scenario Outline: Verify that invalid drone id while updating sortie details results in error.
    Given a drone with id <drone id> is present
    And instruction with id <instruction id> is not present
    When current location of drone is <current location>
    And  current speed of drone is <current speed>
    And status of drone is <status>
    And estimated time of arrival is <estimated time>
    And warehouse location is <warehouse location>
    And destination location is <destination location>
    And drone sends sortie update request to webhook
    Then sortie details fails to update with error <error>
    Examples:
      | drone id | instruction id | current location | destination location | warehouse location | status      | estimated time | current speed | error                 |
      | 5        | -1             | 30.21,21.02      | 40.25,30.54          | 30.21,22.32        | in_progress | 20.45          | 45.50         | InstructionIdNotFound |