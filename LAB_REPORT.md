# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/kafies/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | 21.0.12 |
| Maven | 3.10 |
| IDE | VSCode 1.143.1 |
| SonarQube for IDE | 6.1.1 |
| AI tool and exact model | Claude Haiku 5.5 |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 3487dbd

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | S2143 | ParcelService.java:Ln 1, Col 1 | Use the "java.time" API for date and time. |
| 2 | S106 | ParcelService.java:Ln 9, Col 9 | Replace this use of System.out by a logger. |
| 3 | S106 | ParcelService.java:Ln 15, Col 17 | Replace this use of System.out by a logger. |
| 4 | S106 | ParcelService.java:Ln 17, Col 17 | Replace this use of System.out by a logger. |
| 5 | S106 | ParcelService.java:Ln 22, Col 17 | Replace this use of System.out by a logger. |
| 6 | S2143 | ParcelService.java:Ln 1, Col 1 | Use the "java.time" API for date and time. |
| 7 | S1128 | ParcelService.java:Ln 6, Col 8 | Remove this unused import 'java.util.Date'. |
| 8 | S1068 | ParcelService.java:Ln 12, Col 17 | Remove this unused "maxParcels" private field. |
| 9 | S106 | ParcelService.java:Ln 30, Col 17 | Replace this use of System.out by a logger. |
| 10 | S4973 | ParcelService.java:Ln 42, Col 52 | Strings and Boxed types should be compared using "equals()". |
| 11 | S106 | ParcelService.java:Ln 67, Col 17 | Replace this use of System.out by a logger. |
| 12 | S106 | ParcelService.java:Ln 69, Col 17 | Replace this use of System.out by a logger. |
| 13 | S2095 | ParcelService.java:Ln 76, Col 33 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 14 | S108 | ParcelService.java:Ln 80, Col 33 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |


## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 23/10/2026 |
| AI tool and exact model | Claude Haiku 5.5 |
| Commit reviewed | 3487dbd |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | attached the files |
| Output file in the reviews folder | parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | `==` compares String references, so pickup never matches a typed code | both | S4973 | Yes. This is a real bug, and `equals()` is the correct fix. |
| 2 | ParcelService.java:76-80 | `FileWriter` is never closed, which leaks a handle and risks data loss | both | S2095 | Yes. Try-with-resources is the right fix. |
| 3 | ParcelService.java:80-81 | Empty `catch` silently swallows `IOException` | both | S108 | Yes. Failed saves go unnoticed. |
| 4 | ParcelService.java:6 | Unused import `java.util.Date` | both | S1128 | Yes. Trivial, but correct. |
| 5 | ParcelService.java:12 | `maxParcels` field is never used | both | S1068 | Yes. The AI review also suggests enforcing the limit, which is the better fix. |
| 6 | Lines 9, 15, 17, 22, 30, 67, 69 (ParcelService.java) | `System.out` used in seven places | SonarQube | S106 | Partly. The rule is valid for library code, but a command-line program is expected to print to stdout, so this is not a real defect here. |
| 7 | Line 1 | Suggests using `java.time` instead of `java.util.Date` | SonarQube | S2143 | Yes, but low value. The `Date` import is unused, so deleting it (row 4) is the real fix. |
| 8 | ParcelService.java:39-50 | `pickUp` returns 0 for both an unknown code and a valid zero fee, so `Main` prints "Fee: 0.0" in both cases | AI | Error handling (not flagged by SonarQube) | Yes. This is a real design flaw that SonarQube's rules don't check. |
| 9 | Main.java:11-14 | No input validation for missing words or a non-numeric size | AI | Input validation (not flagged by SonarQube) | Yes. Missing arguments cause exceptions. |
| 10 | ParcelService.java:53, 57, 59, 61 | Magic numbers for the free period and the rates | AI | Magic numbers (SonarQube's S109 would apply, but it isn't in the report) | Yes. Named constants would make the rates easier to change. |
| 11 | Parcel.java:13; ParcelService.java:28, 44, 68 | Status strings repeated as literals | AI | Magic strings (not flagged by SonarQube) | Yes, though minor. An enum would prevent typos. |
| 12 | Main.java:10-26 | Long if/else chain handles every command in `main` | AI | Procedural method cohesion | Yes, though this is a matter of style. |
| 13 | ParcelService.java:9-83 | One class handles storage, pickup, fees, reporting, SMS and persistence | AI | Class cohesion | Yes. |
| 14 | ParcelService.java:24-37 | `storeParcel` mixes locker assignment, SMS and file saving | AI | Procedural method cohesion | Yes, though the steps are arguably all part of storing a parcel. |
| 15 | ParcelService.java:39-50 | `pickUp` mixes search, state change, persistence and fee calculation | AI | Procedural method cohesion | Partly. Mixing persistence and fees is a real concern, but the steps serve one goal. |
| 16 | ParcelService.java:64-72 | `printParcels(boolean)` does two unrelated jobs based on a flag | AI | Logical cohesion | Yes. |
| 17 | Main.java:6-27 | `main` combines the loop, parsing and dispatch | AI | Procedural method cohesion | Yes. This overlaps with row 12. |
| 18 | ParcelService.java:52-62 | `fee` uses none of the class's attributes | AI | Class cohesion | Yes, but low severity. |
| 19 | ParcelService.java:28, 44, 68; Parcel.java:7 | `ParcelService` reads and writes `Parcel.status` directly | AI | Content coupling | Yes. |
| 20 | Main.java:22 | `Main` reaches through `ParcelService` into `Locker` data to count free lockers | AI | Stamp coupling | Partly. The real issue is a Law of Demeter violation, and "stamp coupling" is a loose fit. |
| 21 | ParcelService.java:46 | `fee(parcel, days)` receives a whole `Parcel` but only uses its size | AI | Stamp coupling | Yes, but minor. |
| 22 | Main.java:19, 21; ParcelService.java:64 | The boolean flag controls the method's behaviour | AI | Control coupling | Yes. |
| 23 | Main.java:13 | `new Parcel(...)` takes adjacent Strings, so swapping them compiles without error | AI | Connascence of position | Yes. |
| 24 | ParcelService.java:15-17 | `new Locker(1, 1)` and similar calls rely on argument order | AI | Connascence of position | Yes, but minor. |
| 25 | Locker.java:5; Parcel.java:6; ParcelService.java:56-58 | Integers 1, 2 and 3 mean small, medium and large only by comment | AI | Connascence of meaning (the AI review called it "convention") | Yes. The correct term is connascence of meaning, and an enum would fix it. |
| 26 | Parcel.java:7 | `status` field is `public` | AI | Encapsulation | Yes. |
| 27 | ParcelService.java:20-22 | `getLockers()` returns the internal mutable list | AI | Encapsulation | Yes. |

2.1 Name two problems that only the AI review found.
    Wrong return value for unknown codes (row 8): pickUp returns 0 both for an unknown code and for a valid pickup with no fee so Main prints “Fee: 0.0” in both cases. SonarQube’s rules don’t check error signalling, so it couldn’t catch this. Input validation missing in Main (row 9): Missing words cause ArrayIndexOutOfBoundsException, and a non-numeric size causes NumberFormatException. Neither exception is handled, and no SonarQube rule in the report covers this.
2.2 Name one problem that only SonarQube found, or write none.
    The java.time suggestion (row 7, S2143) is the only finding that appeared only in SonarQube’s report and is also a real improvement. It is low value, though, because the unused Date import is removed by the fix in row 4.
2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.
    Row 20 is the weakest finding. The review labelled Main’s access to Locker data as “stamp coupling”, but stamp coupling means passing a larger structure than a method needs. Here, Main calls getLockers() and filters the result to count free lockers, which is closer to a Law of Demeter violation. The concern is valid, but the label is loose.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Kept | Main prints command results to standard output, which is the program's user interface, not a diagnostic log. |
| 2 | Kept | Same as row 1: Main prints command output for the user. |
| 3 | Kept | Same as row 1: Main prints command output for the user. |
| 4 | Kept | Same as row 1: Main prints the free-locker count for the user. |
| 5 | Fixed | 5c1f73e Remove unused Date import |
| 6 | Fixed | 5c1f73e Remove unused Date import |
| 7 | Fixed | e4d010a Remove unused maxParcels field |
| 8 | Kept | ParcelService prints the SMS notification as user-facing output of the simulated service. |
| 9 | Fixed | 094da4e Fix string comparison in pickUp |
| 10 | Kept | printParcels prints the parcel list as command output for the user. |
| 11 | Kept | Same as row 10: printParcels prints the parcel list as command output for the user. |
| 12 | Fixed | 846f0ee Use try-with-resources and rethrow save errors |
| 13 | Fixed | 846f0ee Use try-with-resources and rethrow save errors |

Number of SonarQube issues that remain after your fixes: 7

The two design problems that you fixed
- Connascence of meaning. The integers 1, 2 and 3 for parcel and locker size are shared by three classes, and only a comment says what they mean (Table 2, row 25).
- Content coupling. ParcelService reads and writes the public field Parcel.status directly (Table 2, rows 19 and 26).

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Connascence of meaning. The integers 1, 2 and 3 for parcel and locker size are shared by three classes, and only a comment says what they mean (Table 2, row 25). | Replaced the integer size codes with a `Size` enum nested in `Parcel`, and updated `Locker`, `ParcelService` and `Main`. | a6161a5 Replace integer size codes with Size enum |
| Content coupling. `ParcelService` reads and writes the public field `Parcel.status` directly (Table 2, rows 19 and 26). | Made `status` private and added `getStatus`, `markStored` and `markPickedUp`. `ParcelService` now calls these methods. | 560e90a Encapsulate Parcel status behind methods |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
    SMS to 6501: parcel P1 is in locker 2
    Locker 2
    Fee: 30.0
    P1 6501 PICKED_UP
    Free lockers: 3

    parcels.csv:
    P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 2026-10-24 | 2026-10-24 |
| AI tool and exact model | Claude Haiku 5.5 | Claude Haiku 5.5 |
| Prompt | B as given | C as given |
| Builds with mvn package (yes or no) | yes | yes |
| Number of classes, interfaces, enums and records | 1: class `Main` | 11: 5 classes, 1 interface (`RideRepository`), 2 enums (`MemberType`, `Station`), 3 records (`Ride`, `Rental`, `Bike`) |
| SonarQube issues, total | 13 | 12 |
| SonarQube issues, by rule | S2386: 4; S106: 9 | S106: 12 |
| AI review findings in Check 1 | 8 (3 Should fix, 5 Note) | 5 (1 Should fix, 4 Note) |
| AI review findings in Check 2 | 17 (1 Must fix, 7 Should fix, 9 Note) | 15 (2 Should fix, 13 Note) |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.
    No. The export contains only S2386 (“Make this member ‘protected’”, 4 times) and S106 (“Replace this use of System.out by a logger”, 9 times). Neither rule checks cohesion or coupling. The AI review found the cohesion and coupling problems in Main.java (Check 2, rows 13 to 23).
4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.
    Yes. Check 2, row 27 of the bikeshare-poor review flags bikeStation, renter and renterType at Main.java:17 to 19 as public static, non-final, mutable maps, rated Must fix. The bikeshare-good review has no field-visibility finding (“fields that are not private: Not found”), and a search of its source finds no public static fields apart from main and the two parse methods.
4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?
    I would extend bikeshare-good. The fee rate belongs to the enum, so a visitor is a single new constant, VISITOR(4), in MemberType. The parse method then accepts the new name without other changes. In bikeshare-poor, member types are int codes checked in typeCode, the fee is hard-coded in giveBack as type == STUDENT ? 2 : 3, and the prompts list the types by hand. A visitor would need edits in several places in Main.java.
## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.
    SonarQube checks single-line and small-block rules, such as unused imports, == on strings and System.out calls. It missed the blank member ID and the fee overflow in bikeshare-poor, and it does not check cohesion, coupling or connascence.
5.2 When should you not trust the AI review? Give an example from this lab.
    Check each finding’s label and risk against the code, because the review can be confident and still wrong. In Table 2, row 20 called a Law of Demeter violation “stamp coupling”, which was the right concern under the wrong name.
5.3 What will you do differently in Project Phase 2 after this lab?
    I will run SonarQube on every file and record each issue’s fix or keep decision, and I will check each AI finding against the code before I act on it.
## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Claude Haiku 5.5 | Task 2: review of the starter code | None. The review produced parcelpoint-review.md and changed no code | compared each finding with the code and SonarQube results |
| Claude Haiku 5.5 | Task 3: Suggested fixes for the SonarQube issues in Table 1 and two design fixes from Table 2 (connascence of meaning, and content coupling on `Parcel.status`). I applied the changes and committed them one fix at a time. | `ParcelService.java` (rows 5, 6, 7, 9, 12, 13 and the status change), `Parcel.java` (Size enum and encapsulated status), `Locker.java` (Size type), `Main.java` (Size parsing). Commits: 094da4e, 846f0ee, 5c1f73e, e4d010a, a6161a5, 560e90a. | the project compiled with no errors; Problems panel showed 14 SonarQube issues after the fixes, and I compared them with Table 3; each fix was committed separately and `git log --oneline` shows the six commits; I read each changed line against the fix the AI suggested. |
| Claude Haiku 5.5 | Task 4: generated bikeshare-poor and bikeshare-good | Generated `Bike.java` `BikeInventory.java` `BikeShare.java` `CsvRideRepository.java` `Main.java` `MemberType.java` `Menu.java` `Rental.java` `Ride.java` `RideRepository.java` `Station.java` `Main.java`  | Both projects built with `mvn package`. Counted SonarQube issues for each (13 for poor, 12 for good) and checked the rule breakdown. Ran two AI reviews per project (Check 1 and Check 2) and saved them to the reviews folder. |