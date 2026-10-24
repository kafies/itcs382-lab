1. Files read: `Locker.java`, `Main.java`, `Parcel.java`, `ParcelService.java` (under `parcelpoint/src/main/java`, reviewed from the copies provided in the conversation).

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---|---|---|---|---|---|
| 1 | 1. Code quality | ParcelService.java:42 | `parcel.getCode() == code` compares String references, so pickup never matches a code typed in `Main`. `pickUp` always returns 0. | Must fix | Use `parcel.getCode().equals(code)`. |
| 2 | 1. Code quality | ParcelService.java:76-80 | `FileWriter` is never closed or flushed. Data can be lost and a handle leaks on every save. | Must fix | Use try-with-resources. |
| 3 | 1. Code quality | ParcelService.java:80-81 | Empty `catch` block silently swallows `IOException`, so a failed save goes unnoticed. | Should fix | Log the error or propagate it to the caller. |
| 4 | 1. Code quality | ParcelService.java:6 | Unused import `java.util.Date`. | Note | Delete the import. |
| 5 | 1. Code quality | ParcelService.java:12 | Field `maxParcels` is never used. | Note | Remove it, or enforce the limit in `storeParcel`. |
| 6 | 1. Code quality | ParcelService.java:39-50 | `pickUp` returns 0 both for an unknown code and for a valid pickup within 3 days. `Main` prints "Fee: 0.0" for both. | Should fix | Throw an exception or return an Optional when the code is not found. |
| 7 | 1. Code quality | Main.java:11-14 | No input validation. Missing words cause `ArrayIndexOutOfBoundsException`, a non-numeric size causes `NumberFormatException`, and `split(" ")` breaks on repeated spaces. | Should fix | Check argument count, use `split("\\s+")`, and catch parse errors. |
| 8 | 1. Code quality | ParcelService.java:53, 57, 59, 61 | Magic numbers: the free period 3 and the rates 10, 15, 20 are repeated literals in `fee`. | Should fix | Define named constants such as `FREE_DAYS` and `RATE_SMALL`. |
| 9 | 1. Code quality | ParcelService.java:28, 44, 68; Parcel.java:13 | Status strings "STORED", "PICKED_UP" and "NEW" are repeated as literals. "STORED" appears twice. | Note | Use an enum for parcel status. |
| 10 | 1. Code quality | Main.java:10-26 | Command handling is one long if/else chain inside `main`, mixing input parsing, dispatch and output. | Note | Move each command into its own method, or use a switch. |
| 11 | 2. Class cohesion | ParcelService.java:9-83 | `ParcelService` covers storage, pickup, fee calculation, reporting, SMS notification and file persistence. | Should fix | Move fee calculation and persistence into separate classes. |
| 12 | 2. Method cohesion (procedural) | ParcelService.java:24-37 | `storeParcel` sequences unrelated steps: finding a locker, assigning it, sending an SMS and saving to file. | Should fix | Have the caller send the SMS and call save. |
| 13 | 2. Method cohesion (procedural) | ParcelService.java:39-50 | `pickUp` sequences a search, a state change, persistence and a fee calculation. | Should fix | Keep the state change here and move persistence and fee calculation to the caller. |
| 14 | 2. Method cohesion (logical) | ParcelService.java:64-72 | `printParcels` uses the boolean `forStaff` to choose between two unrelated outputs. | Should fix | Split into `printStaffList` and `printWaitingList`. |
| 15 | 2. Method cohesion (procedural) | Main.java:6-27 | `main` combines the program loop, input parsing and command dispatch. | Note | Split command handling into small methods. |
| 16 | 2. Class cohesion | ParcelService.java:52-62 | `fee` uses only its parameters and none of the class's attributes, which is weak class cohesion. | Note | Make `fee` static or move it to a fee calculator. |
| 17 | 2. Coupling (content) | ParcelService.java:28, 44, 68; Parcel.java:7 | `ParcelService` reads and writes the public field `Parcel.status` directly. This is content coupling and also name connascence on `status`. | Should fix | Add behaviour methods such as `parcel.markStored()` and `parcel.markPickedUp()`. |
| 18 | 2. Coupling (stamp) | Main.java:22 | `Main` gets the internal lockers list and filters it, reaching through `ParcelService` to `Locker`. | Should fix | Add `countFreeLockers()` to `ParcelService`. |
| 19 | 2. Coupling (stamp) | ParcelService.java:46 | `fee(parcel, days)` passes a whole `Parcel`, but `fee` reads only `getSize()`. | Note | Pass `parcel.getSize()` as an int. |
| 20 | 2. Coupling (control) | Main.java:19, 21; ParcelService.java:64 | `printParcels(boolean)` lets the caller's flag choose the method's behaviour. | Should fix | Split into two methods, as in row 14. |
| 21 | 2. Connascence (position) | Main.java:13 | `new Parcel(words[1], words[2], ...)` passes two adjacent Strings. Swapping code and studentId compiles without error. | Should fix | Use a named factory method or a small parsed-input record. |
| 22 | 2. Connascence (position) | ParcelService.java:15-17 | `new Locker(1, 1)`, `new Locker(2, 2)`, `new Locker(3, 3)` rely on argument order. | Note | Name the constructor parameters or use a builder. |
| 23 | 2. Connascence (convention) | Locker.java:5; Parcel.java:6; ParcelService.java:56-58 | The integers 1, 2 and 3 mean small, medium and large only by comment. | Should fix | Replace with an enum such as `Size`. |
| 24 | 2. Encapsulation (fields) | Parcel.java:7 | Field `status` is `public`. | Should fix | Make it private and expose it through methods. |
| 25 | 2. Encapsulation (collections) | ParcelService.java:20-22 | `getLockers()` returns the internal mutable list, so callers can change it. | Should fix | Return `List.copyOf(lockers)` or an unmodifiable view. |

Common coupling: not found.

Findings per check:
- Check 1 (Code quality): 10
- Check 2 (Cohesion, coupling and connascence): 16