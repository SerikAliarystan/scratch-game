# Scratch Game Service

## Overview

This project is a simple scratch game where users can place a bet and see if they win based on a generated matrix of symbols. The game uses both standard and bonus symbols, which are defined in a configuration file. The result of each spin is calculated based on predefined winning combinations.

## Requirements

- **JDK**: 21 or higher
- **Build Tool**: Maven
- **Libraries**: Jackson for JSON parsing (feel free to replace with another library if preferred)

## Game Description

In this scratch game, a matrix (e.g., 3x3) is generated based on probabilities defined in a configuration file. The matrix consists of standard symbols and may contain bonus symbols. Users place a bet, and the game checks if the user has won based on the generated matrix and the winning combinations described in the configuration.

### Symbols

- **Standard Symbols**: Determine if the user wins or loses based on matching combinations in the matrix.
- **Bonus Symbols**: Modify the final reward but only apply if there’s already a win from standard symbols.

### Configuration File

The game behavior is controlled by a JSON configuration file that specifies:
- The matrix size (number of rows and columns).
- The symbols, their types, and reward multipliers.
- The probabilities of each symbol appearing in the matrix.
- The winning combinations and their reward multipliers.

### Example Configuration

```json
{
    "columns": 3,
    "rows": 3,
    "symbols": {
        "A": {
            "reward_multiplier": 50,
            "type": "standard"
        },
        ...
        "10x": {
            "reward_multiplier": 10,
            "type": "bonus",
            "impact": "multiply_reward"
        },
        "MISS": {
            "type": "bonus",
            "impact": "miss"
        }
    },
    "probabilities": {
        "standard_symbols": [
            {
                "column": 0,
                "row": 0,
                "symbols": {
                    "A": 1,
                    "B": 2,
                    ...
                }
            }
        ],
        "bonus_symbols": {
            "symbols": {
                "10x": 1,
                "MISS": 5
            }
        }
    },
    "win_combinations": {
        "same_symbol_3_times": {
            "reward_multiplier": 1,
            "when": "same_symbols",
            "count": 3,
            "group": "same_symbols"
        },
        ...
    }
}
```
## Building and Running the Game

### Building

To build the project, run the following Maven command:

```bash
mvn clean package
```

This will compile the code and create a runnable JAR file in the `target` directory.

### Running the Game

You can run the game with the following command:

```bash
java -jar target/ScratchGame-1.0.jar --config <path-to-config-file.json> --betting-amount <betting-amount>
```

Just replace `<path-to-config-file.json>` with the path to your JSON configuration file and `<betting-amount>` with the amount you want to bet.

#### Example

```bash
java -jar target/ScratchGame-1.0.jar --config config.json --betting-amount 100
```

## Example Output

```json
{
  "matrix": [
    ["A", "A", "B"],
    ["A", "+1000", "B"],
    ["A", "A", "B"]
  ],
  "reward": 6600,
  "applied_winning_combinations": {
    "A": ["same_symbol_5_times", "same_symbols_vertically"],
    "B": ["same_symbol_3_times", "same_symbols_vertically"]
  },
  "applied_bonus_symbol": "+1000"
}
```
### Testing

This project uses JUnit 5 for testing and Mockito for mocking dependencies. You can run the tests with:

```bash
mvn test
```
This command will execute all unit tests and show you the results.

## Maven Dependencies

Here's a list of key dependencies used in this project:

- **Jackson**: For JSON parsing
- **Lombok**: To reduce boilerplate code (like getters/setters)
- **Hibernate Validator**: For validating configuration inputs
- **JUnit 5**: For writing and running tests
- **Mockito**: For mocking objects in tests

### Plugins

- **Maven Shade Plugin**: Used to create a single executable JAR file with all dependencies included.

## Additional Notes

- **Customization**: Feel free to tweak the configuration file to test different scenarios. The game's logic is fully driven by this config, so you can easily adjust the symbols, probabilities, and winning combinations.
- **No External Frameworks**: The assignment guidelines suggest avoiding additional frameworks like Spring, so this implementation sticks to plain Java and Maven.

## Contact

If you have any questions or run into issues, feel free to reach out to me at [aliarystan@gmail.com]. I'm happy to help!

