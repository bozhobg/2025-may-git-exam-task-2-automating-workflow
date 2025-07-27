## Automating Workflow with Git Hooks and CI/CD
Integration

1. Initial state of project - succeeding tests and formatted code (Maven spotless plugin)
![1-initial-state-1.png](./readme-files/1-initial-state-1.png)
![1-initial-state-2.png](./readme-files/1-initial-state-2.png)
2. Adding commit-msg hook to look for format of '[TASK-123]'
    .git/hooks/commit-msg
```
#!/bin/sh
# commit-msg hook script to enforce message format
if ! grep -q '^\[TASK-[0-9]\+\]' "$1"; then
  echo "Commit message must start with a TASK ticket reference, e.g., [TASK-123]: Your message"
  exit 1
fi
```
![2-commit-msg-demo.png](./readme-files/2-commit-msg-demo.png)
3. Checking using code formatter with pre-commit hook
```
#!/bin/sh
# Run spotless before commit
echo "Running spotless code formatting check..."

mvn spotless:check

if [ $? -ne 0 ]; then
	echo "Spotless errors found. Commit aborted."
	echo "Run: mvn spotless:apply to reformat code to standard"
	exit 1
fi
```
![3-pre-commit-format-check.png](./readme-files/3-pre-commit-format-check-1.png)
![3-pre-commit-format-check.png](./readme-files/3-pre-commit-format-check-2.png)
4. Check if tests pass before push
```
#!/bin/sh
# Run unit tests before pushing 

echo "Running test before push"

mvn test

if [ $? -ne 0 ]; then
	echo "Tests failed. Push aborted."
	exit 1
fi
```
![4-pre-push-test-check-1.png](./readme-files/4-pre-push-test-check-1.png)
![4-pre-push-test-check-2.png](./readme-files/4-pre-push-test-check-2.png)
5. Simulating pull request with broken tests - GH Actions to run tests on pull request
ci.yaml:
```
name: CI Pipeline
on: [pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4
      - name: Set up JDK Amazon Corretto 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'corretto'
          cache: maven
      - name: Run the Maven Verify phase
        run: mvn --batch-mode --update-snapshots verify
```
![5-pull-request-runs-test-1.png](./readme-files/5-pull-request-runs-test-1.png)
![5-pull-request-runs-test-2.png](./readme-files/5-pull-request-runs-test-2.png)
![5-pull-request-runs-test-3.png](./readme-files/5-pull-request-runs-test-3.png)
6. git log and git reflog
![6-git-log-git-reflog.png](./readme-files/6-git-log-git-reflog.png)