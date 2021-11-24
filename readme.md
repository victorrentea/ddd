# Agile Project Support System
An exercise of refactoring an Anemic Domain Model towards Domain-Driven Design

Intro Reading:
- https://www.infoq.com/minibooks/domain-driven-design-quickly/

Tip for accidental reader: see the branches for how the code evolves.

## Product
- The user can create a new product
- The code of the product must be of 3 letters and has to be unique

## Backlog Item
- Users can create and edit Product Backlog Items
- System must block attempts to edit the same Backlog Item by two users at the same time

## Sprint
- The user can schedule the next Sprint providing the planned end date
- The Sprint must store the actual start and end dates
- A new Backlog Item can be added to a Sprint Backlog (<> Product Backlog) after being estimated in Function Points (FP)
- A developer can start working on a Backlog Item
- A developer can report hours consumed on a Backlog Item, if that is started in the current sprint
- A developer can finish a Backlog Item
- After an Item is completed, it can never be edited anymore
- If all Backlog Items of a Sprint are DONE before the planned end date, the team celebrates by a day of Mob Refactoring :)
- The sprint can be ended at any actual date
- Sprints of a Product are assigned consecutive IDs
!- If there are Backlog Items NOT DONE at the end of the sprint, the items return to the product backlog and the PO is emailed automatically (to shield devs from misplaced shame)
- After a Sprint ended, the system can generate metrics about it like velocity, consumed hours, done FP
- The user-visible ID of a sprint is <projectCode>-<iterationNumber>. Examples: PNM-3, KRI-12 

## Release
- A user can plan a release of any finished Sprint, for a certain date
- You can only release a *later* sprint than the one previously released
- The changelog of a release is automatically composed from the backlog items shipped since the last release
- On release, the sprint is marked accordingly
- A release can be Major or Minor. In both cases, versions must be consecutive. Examples: 
  - 1.0 -(major)-> 2.0
  - 1.2 -(major)-> 2.0
  - 2.0 -(minor)-> 2.1

