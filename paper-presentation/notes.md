# Stream Processing of XPath Queries with Predicates (2003)
A. Gupta, D. Suciu

## What is the problem?

Various applications require filtering streams of XML (XML packet routing, dissemination of information, notification systems) and thus can have thousands to hundreds of thousands of XPath filter queries running on the stream to filter the data.

## What is the authors' approach?
- Deterministic (each SAX event processed in O(1) time, just a lookup in table) pushdown automata-based approach they call XPush.
- Basically, combines a bunch of automata for each query into one large pushdown automata
-- Each filter converteted to Alternating Finite Automata (AFA), a nondeterministic FA
-- (Optimization section?) Number of states in XPush machine is exponential in the number of different values. Thus, they compute the required states lazily at runtime. Expensive at first, but free when reused. (use phone number example, "Lazy construction of states" in slides)

- Predicates can be shared between different queries

## Optimizations
- Reduce number of XPush states (top-down pruning)
- Reduce number of AFA states pr. XPush state (order optimization)
- Precomputing XPush states before processing any XML stream (training)

## Evaluation
- How effective can the XPush machine be?
- What are the memory requirements of the lazy XPush machine?
- How close the performance of the XPush machine is to the ideal performance, when it doesn’t have to compute any states at runtime?
- How effective are the optimization techniques?

## Limitations with this approach?
- Very, very limited set of XPath, doesn't even take XQuery filters into consideration. Only boolean queries.
- 1 document at a time...?
- Creation of states is very expensive, thus they have an optimization section
- The optimization approach of eliminiating redundant computation among several queries is not applicable to databases, where queries are executed concurrently over different XML steams (http://www.cis.upenn.edu/~zives/cis650/fulltext.pdf)
- Doesn't explain how XPush adapts to relational data (or how to extract multiple bindings in tuples)

## Future work
- Updates to XPath workload

## Notes
- There is a streaming XQuery processor: http://www-nishio.ist.osaka-u.ac.jp/vldb/archives/public/website/2003/papers/S30P01.pdf
- Original paper presentation: https://wiki.aalto.fi/download/attachments/40014804/xpush_presentation.pdf