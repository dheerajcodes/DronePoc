Feature: Send an instruction to a drone and retrieve instruction detail.

  @T_003
  Scenario Outline: Verify that pickup instruction can be sent to a drone.
    Given a drone with id <id> is present
    And drone is not busy
    When a pickup instruction from <warehouse location> to <destination location> is sent to drone
    Then instruction is created successfully
    And drone is busy
    Examples:
      | id      | warehouse location | destination location |
      | drone_5 | 10.20,25.25        | 22.25,30.23          |

  @T_004
  Scenario Outline: Verify that details of an instruction are retrieved.
    Given instruction with id <id> is present
    When instruction details are fetched
    Then details of instruction are displayed
    Examples:
      | id    |
      | ins_1 |

  @T_008 @T_009
  Scenario Outline: Verify that invalid or empty drone id while sending pickup instruction to drone results in error.
    Given a drone with id <id> is not present
    When a pickup instruction from <warehouse location> to <destination location> is sent to drone
    Then instruction is failed with error <error>
    Examples:
      | id  | warehouse location | destination location | error          |
      | abc | 10.20,25.25        | 22.25,30.23          | InvalidDroneId |
      |     | 10.20,25.25        | 22.25,30.23          | EmptyDroneId   |

  @T_010 @T_011
  Scenario Outline: Verify that invalid or empty warehouse location while sending pickup instruction to drone results in error.
    Given a drone with id <id> is present
    When a pickup instruction from <warehouse location> to <destination location> is sent to drone
    Then instruction is failed with error <error>
    Examples:
      | id      | warehouse location | destination location | error                    |
      | drone_5 | 10.0.89,.25        | 22.25,30.23          | InvalidWarehouseLocation |
      | drone_5 |                    | 22.25,30.23          | EmptyWarehouseLocation   |

  @T_012 @T_013
  Scenario Outline: Verify that invalid or empty destination location while sending pickup instruction to drone results in error.
    Given a drone with id <id> is present
    When a pickup instruction from <warehouse location> to <destination location> is sent to drone
    Then instruction is failed with error <error>
    Examples:
      | id      | warehouse location | destination location | error                      |
      | drone_5 | 10.20,25.25        | 22.0.25,.23          | InvalidDestinationLocation |
      | drone_5 | 10.20,25.25        |                      | EmptyDestinationLocation   |

  @T_014 @T_015
  Scenario Outline: Verify that an instruction with unknown or empty type cannot be sent to a drone.
    Given a drone with id <id> is present
    When a <instruction type> instruction from <warehouse location> to <destination location> is sent to drone
    Then instruction is failed with error <error>
    Examples:
      | id      | instruction type | warehouse location | destination location | error                  |
      | drone_5 | unknown          | 10.20,25.25        | 22.25,30.23          | UnknownInstructionType |
      | drone_5 |                  | 10.20,25.25        | 22.25,30.23          | EmptyInstructionType   |

  @T_016
  Scenario Outline: Verify that sending pickup instruction to a busy drone results in error.
    Given a drone with id <id> is present
    And drone is busy
    When a pickup instruction from <warehouse location> to <destination location> is sent to drone
    Then instruction is failed with error <error>
    Examples:
      | id      | warehouse location | destination location | error     |
      | drone_5 | 10.20,25.25        | 22.25,30.23          | BusyDrone |

  @T_017
  Scenario Outline: Verify that invalid id while retrieving instruction details results in error.
    Given instruction with id <id> is not present
    When instruction details are fetched
    Then details of instruction are not found
    Examples:
      | id  |
      | abc |