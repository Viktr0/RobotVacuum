# Navigation System for Robot Vacuum

<p align="center">
  <img width=60% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/70d72969-7265-4c04-aed3-add6d75a4a32"/>
</p>

This was a home assignment created by Ádám Balassa, Árony Derényi and Viktor Horváth.
The task was to develop a robot vacuum cleaner type navigation system in Java.

## Solution

### Components of the Application

<p align="center">
  <img width=90% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/f55f848a-6af6-4e90-9f58-da71c469c8f0"/>
</p>

The project consists of two modules, these are logic and virtual components.
The logic, which contains the components necessary for the operation of the robot vacuum cleaner, does not depend on the Virtual module representing the simulation.
On the other hand, several components of the Virtual module depend on the logic of the robot vacuum cleaner (e.g.: View).

The robot vacuum cleaner only depends on the interface of its hardware, this is defined in its module in the project and implemented in the Virtal module.
These interfaces are realized by the simulation, its responsibility is the appropriate degree of abstraction of reality.

### Simulation

<p align="center">
  <img width=70% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/44f97a9d-d802-41b6-bfc5-2ee292ac7b84"/>
</p>

The components of the simulation define all classes that are responsible for creating the simulation environment.
Its components depend on the robot vacuum, but not on the View.
All communication between the simulation environment and views takes place through the getter calls and subscriptions of the view.

Many virtual components therefore follow the Observable pattern.
They define a Listener interface, which has 1 notifyData method, of course for the corresponding Data.
It is possible to unsubscribe from them using the add and remove listener methods.

During the simulation, the world is described in configuration files, in which polygon data is listed.

The 3 most important components of the simulation are 
* the world,
* the virtual hardware
* and the factory of the simulation.

### View

<p align="center">
  <img width=90% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/4ee9a7f5-d961-4401-8939-934c617660e8"/>
</p>

The basis of the continuous world emerges and the interpreted world of the robot vacuum cleaner is superimposed on it. 
The square grid world known by the robot vacuum cleaner is displayed with some sort of translucent coloring. 
Purely black or white areas or shapes are not recognized by the vacuum cleaner.
<p align="center">
  <img width=40% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/384fa43c-275e-4c39-9172-1ec3d9587dcd"/>
  <img width=40% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/0504e258-dba0-4bbc-9944-9c6c42366d86"/>
</p>

Components:
* **Vacuum cleaner**: red solid circle
* **Laser of the sensor**: blue, vacuum-centered radial sections
* **Obstacle**: black
* **Fields**:
  * **Unknown**: white
  * **Unreachable**: red
  * **Reachable**: dark and light blue
* **Target**: Green circle

### Logic



### Navigation

## Installation

Dependencies:
* [RxJava](https://mvnrepository.com/artifact/io.reactivex.rxjava2/rxjava) (2.2.21) - Reactive Extensions for Java
* [JUnit Jupiter API](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api) (5.10.1) - JUnit Jupiter is the API for writing tests using JUnit 5
* [Reactive Streams](https://mvnrepository.com/artifact/org.reactivestreams/reactive-streams) (1.0.3) - A Protocol for Asynchronous Non-Blocking Data Sequence
 
### Steps for adding JAR files in IntelliJ IDEA

1. Click **File** from the toolbar
2. Select **Project Structure** option
3. Select **Modules** at the left panel
4. Select **Dependencies** tab
5. Select **+** icon
6. Select **JARs or directories** option
7. Add the necessary JARs
