# Agile Project Support System
An exercise of refactoring an Anemic Domain Model towards Domain-Driven Design

Intro Reading:
- https://www.infoq.com/minibooks/domain-driven-design-quickly/

Tip for accidental reader: see the branches for how the code evolves.

## Product
- The user can create a new product
- The code of the product must be of 3 uppercase letters and has to be unique system-wide

## Backlog Item
- Users can create and edit Product Backlog Items
- System must block attempts to edit the same product Backlog Item by two users at the same time (concurrent access)
- Once an item is completed in a Sprint, it cannot be edited anymore

## Sprint
- The user can schedule the next Sprint providing the planned end date
- The Sprint must store the actual start and end dates
- Given a Backlog Item was estimated in Function Points(),
  it can be added to a Sprint Backlog
- A developer can start working on a Backlog Item
- A developer can report hours consumed on a started Backlog Item
- A developer can finish a Backlog Item
- If all Backlog Items of a Sprint are DONE before the planned end date,
  the team celebrates by spending the day Mob Refactoring :)
- The sprint can be ended at any actual date
- Sprints of a Product are assigned consecutive IDs
- After a Sprint ended, the system can generate metrics about it like velocity, consumed hours, done FP
- The user-visible ID of a sprint is <projectCode>-<iterationNumber>. Examples: PNM-3, KRI-12
- **hard** If some items are NOT DONE at the end of the sprint,
  they return to the product backlog so that future sprints can continue it again later.
  In addition, the PO is emailed automatically (to shield devs from apparent shame)

## Release
- A user can plan a release of any finished Sprint, for a certain date
- You cannot release sprints previous to the latest released one
- The changelog of a Release should list all backlog items completed since the last release
- A release can be Major or Minor. In both cases, versions must be consecutive. Examples:
  - 1.0 -(major)-> 2.0
  - 1.2 -(major)-> 2.0
  - 2.0 -(minor)-> 2.1

