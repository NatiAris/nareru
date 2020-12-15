# nareru

A simple (for now) piece of [Spaced Repetition] Software.

## How to run

In the root directory, use `sbt run`
The app uses Play's default [port 9000]

## Core concepts

The core concepts, cards and notes, might be familiar to those who used [Anki].

A _note_ is a self-contained piece of information.
The application currently supports exclusively text-only notes, but in general notes can include pictures and audio, too.

A _card_ is a "view" of a note.
There can be one or more views of the same note, picking different parts of information from the same note to make separate flashcards.

Notes are classified into _note types_ that reflect common patterns in card creation.
Currently supported note types are
- AB ("two-sided" note, that translates into a pair of cards A -> B and B -> A)
- QA ("one-sided" note, the simplest note possible, corresponds to one Q -> A card)
- Seq ("sequential" note, creates cards for pairs of consequent elements in a sequence)

<details>
<summary>Implementation details</summary>

The [abstraction for a card] is very simple, a card has
- a unique id
- the respective note id
- front side
- back side

The [abstraction for a note] is even simpler, a note has
- a unique id
- a note type

[Note types] are specific to the card creation interface.
Only type name is persisted after the cards are created.

</details>

## Scope of the project

The app consists of
- Card creation interface
- Review interface
- Browser

Scheduling, which is essential for any flashcard software, is "lazy".
There is no background process that needs to perform scheduled jobs.

### Card creation interface

This is where you create cards.
You need to choose a note type and specify the contents.

### Card review interface

You see front side ("question"), try to remember the back side ("answer").
Once you're ready, you open the spoiler and click one of the buttons.

### The Browser

This, for now, lists all cards, read-only.

## Dependencies

- [Play] 2.8.5
- [Play-Slick] 5.0.0
- Postgres
- H2 (for tests)

## Project structure

The classy MVC trio
- `models`
- `views`
- `controllers`

Separate modules for different parts of the problem domain
- `cardmaker` (card creation)
- `coach` (scheduling)
- `reviewer` (review)

## Further development

### High Priority

- [ ] Tests
  - [ ] Compile-time DI

### Normal Priority

- [ ] User authentication
- [ ] User-level configuration
- [ ] File upload (bulk card creation)
- [ ] `.csv` and `.xls` parsers
- [ ] Card edition from the browser

### Low Priority

- [ ] Make it deployable
- [ ] Markdown support on front end
- [ ] Hotkey support, at least for reviews
- [ ] Batch load of cards for review

### To Consider

- [ ] Use separate `sbt` modules for components
- [ ] Merge `coach` and `reviewer` modules


[abstraction for a card]: ./app/models/Card.scala
[abstraction for a note]: ./app/models/Note.scala
[Anki]: https://docs.ankiweb.net/#/getting-started?id=key-concepts
[Note types]: ./app/cardmaker/Note.scala
[Port 9000]: localhost:9000
[Spaced Repetition]: https://en.wikipedia.org/wiki/Spaced_repetition
