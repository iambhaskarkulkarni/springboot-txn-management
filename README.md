# springboot-txn-management
Spring Boot Transaction Management Demonstration 

## Transaction propagations

**REQUIRED :** Join an existing transaction or create a new one if not exist.

**REQUIRES_NEW :** Always create a new transaction, suspending if any existing transaction.

**MANDATORY :** Require an existing transaction, if nothing found it will throw exception.

**NEVER:** Ensure the method will run without transaction, throw an exception if found any.

**NOT_SUPPORTED:** Execute method without transaction, suspending any active transaction .

**SUPPORTS:** Supports if there is an active transaction, if not executes without transaction .

**NESTED :** Execute within a nested transaction, allowing nested transaction to rollback independently if there is any exception without impacting outer transaction .

