Feature: Send an instruction to a drone and retrieve instruction detail.

  @T_003
  Scenario Outline: Verify that pickup instruction can be sent to a drone.
    Given endpoint is New Instruction Service
    And request method is POST
    And request accepts JSON
    And request has JSON content
    And request parameter drone-id is <Drone ID>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter destination-location is <Destination Location>
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And new instruction is created successfully
    Examples:
      | Drone ID       | Warehouse Location       | Destination Location       | Status |
      | T_003_DRONE_ID | T_003_WAREHOUSE_LOCATION | T_003_DESTINATION_LOCATION | OK     |

  @T_004
  Scenario Outline: Verify that details of an instruction are retrieved.
    Given endpoint is Instruction Detail Service
    And endpoint url parameter instruction-id is <Instruction ID>
    And request method is GET
    And request accepts JSON
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And response contains instruction details
    Examples:
      | Instruction ID       | Status |
      | T_004_INSTRUCTION_ID | OK     |

  @T_008 @T_009
  Scenario Outline: Verify that invalid or empty drone id while sending pickup instruction to drone results in error.
    Given endpoint is New Instruction Service
    And request method is POST
    And request accepts JSON
    And request has JSON content
    And request parameter drone-id is <Drone ID>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter destination-location is <Destination Location>
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And instruction is failed with error <Error>
    Examples:
      | Drone ID       | Warehouse Location       | Destination Location       | Status               | Error          |
      | T_008_DRONE_ID | T_008_WAREHOUSE_LOCATION | T_008_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | InvalidDroneId |
      | T_009_DRONE_ID | T_009_WAREHOUSE_LOCATION | T_009_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | EmptyDroneId   |

  @T_010 @T_011
  Scenario Outline: Verify that invalid or empty warehouse location while sending pickup instruction to drone results in error.
    Given endpoint is New Instruction Service
    And request method is POST
    And request accepts JSON
    And request has JSON content
    And request parameter drone-id is <Drone ID>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter destination-location is <Destination Location>
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And instruction is failed with error <Error>
    Examples:
      | Drone ID       | Warehouse Location       | Destination Location       | Status               | Error                    |
      | T_010_DRONE_ID | T_010_WAREHOUSE_LOCATION | T_010_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | InvalidWarehouseLocation |
      | T_011_DRONE_ID | T_011_WAREHOUSE_LOCATION | T_011_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | EmptyWarehouseLocation   |

  @T_012 @T_013
  Scenario Outline: Verify that invalid or empty destination location while sending pickup instruction to drone results in error.
    Given endpoint is New Instruction Service
    And request method is POST
    And request accepts JSON
    And request has JSON content
    And request parameter drone-id is <Drone ID>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter destination-location is <Destination Location>
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And instruction is failed with error <Error>
    Examples:
      | Drone ID       | Warehouse Location       | Destination Location       | Status               | Error                      |
      | T_012_DRONE_ID | T_012_WAREHOUSE_LOCATION | T_012_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | InvalidDestinationLocation |
      | T_013_DRONE_ID | T_013_WAREHOUSE_LOCATION | T_013_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | EmptyDestinationLocation   |

  @T_014 @T_015
  Scenario Outline: Verify that an instruction with unknown or empty type cannot be sent to a drone.
    Given endpoint is New Instruction Service
    And request method is POST
    And request accepts JSON
    And request has JSON content
    And request parameter drone-id is <Drone ID>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter instruction-type is <Type>
    And request parameter destination-location is <Destination Location>
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And instruction is failed with error <Error>

    Examples:
      | Drone ID       | Warehouse Location       | Type       | Destination Location       | Status               | Error                  |
      | T_014_DRONE_ID | T_014_WAREHOUSE_LOCATION | T_014_TYPE | T_014_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | UnknownInstructionType |
      | T_015_DRONE_ID | T_015_WAREHOUSE_LOCATION | T_015_TYPE | T_015_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | EmptyInstructionType   |

  @T_016
  Scenario Outline: Verify that sending pickup instruction to a busy drone results in error.
    Given endpoint is New Instruction Service
    And request method is POST
    And request accepts JSON
    And request has JSON content
    And request parameter drone-id is <Drone ID>
    And request parameter warehouse-location is <Warehouse Location>
    And request parameter destination-location is <Destination Location>
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And instruction is failed with error <Error>
    Examples:
      | Drone ID       | Warehouse Location       | Destination Location       | Status               | Error     |
      | T_016_DRONE_ID | T_016_WAREHOUSE_LOCATION | T_016_DESTINATION_LOCATION | UNPROCESSABLE_ENTITY | BusyDrone |

  @T_017
  Scenario Outline: Verify that invalid id while retrieving instruction details results in error.
    Given endpoint is Instruction Detail Service
    And endpoint url parameter instruction-id is <Instruction ID>
    And request method is GET
    And request accepts JSON
    When request is sent
    Then response is received with status <Status>
    And response has JSON content
    And response does not contain instruction details
    Examples:
      | Instruction ID       | Status    |
      | T_017_INSTRUCTION_ID | NOT_FOUND |