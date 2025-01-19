# springboot-txn-management
Spring Boot Transaction Management Demonstration 

## Transaction propagations

1. **_REQUIRED_ :** Join an existing transaction or create a new one if not exist.

2. **_REQUIRES_NEW_ :** Always create a new transaction, suspending if any existing transaction.

3. **_MANDATORY_ :** Require an existing transaction, if nothing found it will throw exception.

4. **_NEVER_:** Ensure the method will run without transaction, throw an exception if found any.

5. **_NOT_SUPPORTED_:** Execute method without transaction, suspending any active transaction .

6. **_SUPPORTS_:** Supports if there is an active transaction, if not executes without transaction .

7. **_NESTED_ :** Execute within a nested transaction, allowing nested transaction to rollback independently if there is any exception without impacting outer transaction .

