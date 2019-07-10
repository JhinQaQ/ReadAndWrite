# ReadAndWrite

 readers-writers problems ，use semaphore.
 
 Simplest Reader Writer Problem
The simplest reader writer problem which uses only two semaphores and doesn't need an array of readers to read the data in buffer.

Please notice that this solution gets simpler than the general case because it is made equivalent to the Bounded buffer problem, and therefore only N readers are allowed to enter in parallel, N being the size of the buffer.

Reader
```
do {
	wait(read)
	............
	reading data
	............
	signal(write)
} while(TRUE);
```

Writer
```
do {
	wait(write)
	.............
	writing data
	.............
	signal(read)
} while(TRUE);
```
Algorithm
1. Reader will run after Writer because of read semaphore.
2. Writer will stop writing when the write semaphore has reached 0.
3. Reader will stop reading when the read semaphore has reached 0.
In writer, the value of write semaphore is given to read semaphore and in reader, the value of read is given to write on completion of the loop.

from wikipedia https://en.wikipedia.org/wiki/Readers%E2%80%93writers_problem
