Feature: Retrieve list of drones as well as details of individual drone.

  @T_001
  Scenario Outline: Verify that list of drones is retrieved.
    Given endpoint is Drone List Service
    And request method is GET
    And request accepts JSON
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And response contains list of drones
    Examples:
      | Status |
      | OK     |

  @T_002
  Scenario Outline: Verify that details of a drone are retrieved.
    Given endpoint is Drone Detail Service
    And endpoint url parameter drone-id is <Drone ID>
    And request method is GET
    And request accepts JSON
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And response contains drone details
    Examples:
      | Drone ID       | Status |
      | T_002_DRONE_ID | OK     |

  @T_006
  Scenario Outline: Verify that empty drone list results in error.
    Given endpoint is Drone List Service
    And request method is GET
    And request accepts JSON
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And response contains drone error <Error>
    Examples:
      | Status    | Error             |
      | NOT_FOUND | DroneListNotFound |

  @T_007
  Scenario Outline: Verify that invalid id while retrieving drone details results in error.
    Given endpoint is Drone Detail Service
    And endpoint url parameter drone-id is <Drone ID>
    And request method is GET
    And request accepts JSON
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And response contains drone error <Error>
    Examples:
      | Drone ID       | Status    | Error               |
      | T_007_DRONE_ID | NOT_FOUND | DroneDetailNotFound |
