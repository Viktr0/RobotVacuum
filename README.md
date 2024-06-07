# Navigation System for Robot Vacuum

<p align="center">
  <img width=60% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/70d72969-7265-4c04-aed3-add6d75a4a32"/>
</p>

This was a home assignment created by [Ádám Balassa](https://github.com/adam-balassa), [Árony Derényi](https://github.com/AronDerenyi) and Viktor Horváth.
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

<p align="center">
  <img width=50% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/64eb259c-1b20-416f-a99e-97c2ed36eac9"/>
</p>

In this module, we implemented the robot vacuum cleaner itself. 
This module has no dependencies on external modules, it only depends on abstraction (hardware interface). 
The robot vacuum cleaner takes the real implementation of hardave in the constructor (of course, it does not know their dynamic type). 
The basic operation of the robot vacuum cleaner is implemented by the RobotVacuum class, all other components in the module are used by this class.

When the start is called, the robot vacuum cleaner forwards these calls to the hardware components and then enters an endless cycle. 
It exits the cycle in response to an asynchronous stop call, then sets its running flag to false and forwards the call to the hardware. 
In the main loop, the following 6 things happen in order: 
1. requesting radar data,
2. updating the interpreted world based on the radar data,
3. finding the next target,
4. calculating movement towards the target,
5. executing movement,
6. processing movement.

Requesting the radar data and executing the movement is of course done by the hardware. 
The other 3 tasks are performed by the 3 main components of the robot vacuum cleaner: 
* the interpreter,
* the motion controller
* and the navigator.

![image](https://github.com/Viktr0/RobotVacuum/assets/47856193/90c90128-9b9d-4a8c-ba6d-ac111687446d)

### Navigation

There are tow implemented algorithms in this module:
* Simple Navigator
* Smart Navigator

#### Simple Navigator

The simple navigator works in three steps. 
First, it maps the world and in the process stores how to get to each point from field to field. 
In the second step, it selects the most ideal of the mapped fields and store it as a goal. 
Finally, it creates a path consisting of (x,y) coordinates that leads from the current position of the vacuum cleaner to the target field, avoiding the walls.

<p align="center">
  <img width=16% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/7eebefa0-743c-4529-9fa7-913a4d14140e"/>
  <img width=16% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/c0c1ef5f-849d-4518-a81b-2d848fb04f9e"/>
  <img width=16% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/62153fb8-cfaa-4801-8301-6b4589af3f78"/>
  <img width=16% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/c0364450-d385-4ad9-98a0-0df88345db76"/>
  <img width=16% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/7ae75234-953d-43dc-8529-517d3a6d9b9a"/>
  <img width=16% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/3a7c3d0c-1ff8-4797-ae62-02617c5c3405"/>
</p>

#### Smart Navigator

The smart navigator uses two helper classes for smart route finding.

One is WorldCrawler, whose task is similar to the first two steps of Simple Navigator. 
Its task, after specifying a condition, is to find the field that fulfills the condition, while storing the route that led to it. 
For this, it needs a starting point from which to start the search and a world in which to search.
The search only runs until we find a suitable field for us.
Using this, we can easily and quickly build the route that the Simple Navigator would find.

After that, we will optimize the route to cut the corners (that can be cut).
For this we use the SubWorld class. 
The SubWorld class needs a world and a rectangle inside the world in which to find the actual shortest path between two arbitrary points
When SubWorld is created, it searches for all the corners in the world (at these points the path will have to be broken) and walls (which the path cannot go through).

<p align="center">
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/4f68201a-fcf5-4955-9ebc-0b307a3554f8"/>
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/37748063-3a24-45b9-b475-b361f7207578"/>
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/24013270-266e-40ec-8c0a-3763222e1b24"/>
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/29c813d1-e8c2-413b-9b04-244725345ac1"/>
</p>

In the next step, we plan a route from the position of the vacuum cleaner to the target point using a *Dijkstra algorithm*. 
In this case, the corners are the nodes we pass through and there is an edge between the corners, the section between which does not intersect a wall.

<p align="center">
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/290d5d66-840e-467c-ba14-50e1e78c8484"/>
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/842fc0dc-6984-44b3-aa03-966f929054a6"/>
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/04eefbb6-5304-4318-b025-56e852421e17"/>
  <img width=20% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/503fad05-f7e8-45c5-9f1f-fceb2ebb4529"/>
</p>

The path found in this way will actually be the shortest path between the robot vacuum cleaner and the goal we consider to be the most optimal in terms of cleaning.
<p align="center">
  <img width=60% src="https://github.com/Viktr0/RobotVacuum/assets/47856193/c9b1285b-4319-42a0-847d-9d0988dacfea"/>
</p>



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
