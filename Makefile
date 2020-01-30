.PHONY: default
default:

.PHONY: dev
dev:
	clojure -A:main

.PHONY: nrepl
nrepl:
	clojure -A:dev:test:nrepl

.PHONY: test/watcher
test/watcher:
	clojure -A:test:watcher
