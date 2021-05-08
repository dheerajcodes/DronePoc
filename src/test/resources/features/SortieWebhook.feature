Feature: Webhook for the drone to update its sortie details.

  @T_005
  Scenario Outline: Verify that drone can update sortie details.
    Given endpoint is Sortie Webhook Service
    And endpoint url parameter drone-id is <Drone ID>
    And request method is PUT
    And request accepts JSON
    And request has JSON content
    And request parameter instruction-id is <Instruction ID>
    And request parameter current-location is <Current Location>
    And request parameter destination-location is <Destination Location>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter status is <Status>
    And request parameter estimated-time is <Estimated Time>
    And request parameter current-speed is <Current Speed>
    When request is sent
    Then response is received with status <Response Status>
    And response has JSON content
    And sortie details are updated successfully
    Examples:
      | Drone ID       | Instruction ID       | Current Location       | Destination Location       | Warehouse Location       | Status       | Estimated Time       | Current Speed       | Response Status |
      | T_005_DRONE_ID | T_005_INSTRUCTION_ID | T_005_CURRENT_LOCATION | T_005_DESTINATION_LOCATION | T_005_WAREHOUSE_LOCATION | T_005_STATUS | T_005_ESTIMATED_TIME | T_005_CURRENT_SPEED | OK              |

  @T_018
  Scenario Outline: Verify that invalid drone id while updating sortie details results in error.
    Given endpoint is Sortie Webhook Service
    And endpoint url parameter drone-id is <Drone ID>
    And request method is PUT
    And request accepts JSON
    And request has JSON content
    And request parameter instruction-id is <Instruction ID>
    And request parameter current-location is <Current Location>
    And request parameter destination-location is <Destination Location>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter status is <Status>
    And request parameter estimated-time is <Estimated Time>
    And request parameter current-speed is <Current Speed>
    When request is sent
    Then response is received with status <Response Status>
    And response has JSON content
    And sortie details failed to update with error <Error>
    Examples:
      | Drone ID       | Instruction ID       | Current Location       | Destination Location       | Warehouse Location       | Status       | Estimated Time       | Current Speed       | Response Status | Error           |
      | T_018_DRONE_ID | T_018_INSTRUCTION_ID | T_018_CURRENT_LOCATION | T_018_DESTINATION_LOCATION | T_018_WAREHOUSE_LOCATION | T_018_STATUS | T_018_ESTIMATED_TIME | T_018_CURRENT_SPEED | NOT_FOUND       | DroneIdNotFound |

  @T_019
  Scenario Outline: Verify that invalid instruction id while updating sortie details results in error.
    Given endpoint is Sortie Webhook Service
    And endpoint url parameter drone-id is <Drone ID>
    And request method is PUT
    And request accepts JSON
    And request has JSON content
    And request parameter instruction-id is <Instruction ID>
    And request parameter current-location is <Current Location>
    And request parameter destination-location is <Destination Location>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter status is <Status>
    And request parameter estimated-time is <Estimated Time>
    And request parameter current-speed is <Current Speed>
    When request is sent
    Then response is received with status <Response Status>
    And response has JSON content
    And sortie details failed to update with error <Error>
    Examples:
      | Drone ID       | Instruction ID       | Current Location       | Destination Location       | Warehouse Location       | Status       | Estimated Time       | Current Speed       | Response Status | Error                 |
      | T_019_DRONE_ID | T_019_INSTRUCTION_ID | T_019_CURRENT_LOCATION | T_019_DESTINATION_LOCATION | T_019_WAREHOUSE_LOCATION | T_019_STATUS | T_019_ESTIMATED_TIME | T_019_CURRENT_SPEED | NOT_FOUND       | InstructionIdNotFound |