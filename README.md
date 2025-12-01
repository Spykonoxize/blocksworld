# BlocksWorld 🧱

Implementation of the classic BlocksWorld AI problem. Simulates a robotic arm manipulating stacked blocks to test planning algorithms like A*, BFS, DFS and Dijkstra. Features dynamic state modeling, constraint satisfaction, data mining with association rules, and pathfinding solvers to resolve spatial constraints.

![BlocksWorld Demo](res/blocksworld.gif)

## 📋 Description

The BlocksWorld problem is a classic planning domain in artificial intelligence. The goal is to rearrange a set of blocks from an initial configuration to a goal configuration using a robotic arm that can only move one block at a time.

This project implements:
- **Modelling**: Constraint-based representation of the blocks world
- **Planning**: Multiple search algorithms (DFS, BFS, Dijkstra, A*)
- **Constraint Programming (CP)**: MAC and Backtrack solvers
- **Data Mining**: Association rule mining using Apriori algorithm

## 🚀 Getting Started

### Prerequisites

- **Java 21** (LTS) or higher
- **Maven** (optional, for building)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Spykonoxize/blocksworld.git
cd blocksworld
```

2. Compile the project:
```bash
mvn clean compile
```

Or manually with javac:
```bash
javac -d build -sourcepath src src/blocksworld/modelling/Executable.java
javac -d build -sourcepath src src/blocksworld/planning/Executable.java
javac -d build -sourcepath src src/blocksworld/cp/Executable.java
javac -d build -sourcepath src src/blocksworld/datamining/Executable.java
```

### Running

**Planning module** (with live GUI simulation):
```bash
java -cp "target/classes;lib/*" blocksworld.planning.Executable
```

**Constraint Programming module**:
```bash
java -cp "target/classes;lib/*" blocksworld.cp.Executable
```

**Data Mining module**:
```bash
java -cp "target/classes;lib/*" blocksworld.datamining.Executable
```

**Modelling module**:
```bash
java -cp "target/classes;lib/*" blocksworld.modelling.Executable
```

## 📁 Project Structure

```
blocksworld/
├── src/
│   ├── blocksworld/          # BlocksWorld specific implementations
│   │   ├── cp/               # Constraint Programming executables
│   │   ├── datamining/       # Data Mining executables
│   │   ├── modelling/        # World modeling
│   │   └── planning/         # Planning algorithms & actions
│   ├── cp/                   # Generic CP solvers
│   ├── datamining/           # Generic data mining algorithms
│   ├── modelling/            # Generic constraint modeling
│   └── planning/             # Generic planning algorithms
├── lib/                      # External JAR dependencies
├── res/                      # Resources (images, gifs)
└── pom.xml                   # Maven configuration
```

## 🔧 Algorithms Implemented

### Planning
| Algorithm | Description |
|-----------|-------------|
| **DFS** | Depth-First Search |
| **BFS** | Breadth-First Search |
| **Dijkstra** | Shortest path algorithm |
| **A*** | A-star with custom heuristics (H1, H2) |

### Constraint Programming
| Solver | Description |
|--------|-------------|
| **MAC** | Maintaining Arc Consistency |
| **Backtrack** | Backtracking with constraint propagation |

### Data Mining
| Algorithm | Description |
|-----------|-------------|
| **Apriori** | Frequent itemset mining |
| **Association Rules** | Rule extraction with confidence/frequency |

## 📊 Example Output

```
DFS =>
[action1, action2, ...]
Node count => 70
Time => 2.0 ms

A* with H2 =>
[action1, action2, action3]
Node count => 11
Time => 2.0 ms
```

## 👥 Authors

- **Spykonoxize**

## 📄 License

This project is for educational purposes.