Project Goal:
I am planning to build a simple 2D spaceflight simulator that would allow a user to launch and navigate a spacecraft around a planet and its orbiting moon. The simulator would use newtonian physics to model gravity, velocity, acceleration, and thrust.

Tech Stack:
- Java 25
- OpenJDK 25
- JavaFX 25
- JavaFX Canvas
- Java Math API
- Maven
- Visual Studio Code
- Git
- Github
- Markdown
- JSON
- JUnit 5

Plan:
1. Initialize the project and create a blank JavaFX window
2. Implement vector math and bodies
- Vector2D.java
- CelestialBody.java
- Spacecraft.java
- constants.java
- test vector math with JUnit tests
3. implement Newtonian gravity
- Gravity.java
- test physics with JUnit tests
4. Implement physics engine
- integrator.java using velocity verlet
- simulation.java to hold the current state
- PhysicsEngine.java to update the vectors at a fixed time step
5. Simple Rendering
- SimulationCanvas.java to render earth as a circle and the ship as a smaller object
- JavaFX animation timer to update the display
6. Spacecraft control and simulation controls
- simple WASD controls for prograde and retrograde and radial burns
- pause simulation and input starting controls.
7. polish and test
- look for edge cases and bugs

