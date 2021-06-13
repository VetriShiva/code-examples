Java 8 features
=========================
Example project with most popular java 8 new features:

* Lambda expressions
* Stream Api
* Functional interfaces
* Default methods in interfaces

```
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 
interfaces.methods

    Default methods   
        Classes can override.
        Interface may contain more than 1 default method.
        Can't override {@link Object} methods.
    Static methods
        Call like all statis methods: Classname.method()
        Can not be overridden
	 
Optional	 
    //Of some value, can't be null!
    Optional<String> of = Optional.of("string");
    //Of any value, can be null
    Optional<String> ofNullable = Optional.ofNullable(null);	
    
    System.out.println(Optional.of(null).isPresent()); //Produce NullPointerException
	
Stream		
    Stream lifecycle goes on 3 steps:
        Creating stream
        Use some operations with stream (filter, order...)
        Final terminal operation and returning the result.
	 
	 
mathOperations	 
    List<Integer> integers = Arrays.asList(1,2,3);
    IntSummaryStatistics stats = integers.stream().mapToInt((x)->x).summaryStatistics();
    System.out.println(stats);	 
    
    // mapInt to List
    List<Integer> prices = phoneList.stream().mapToInt(p->p.getPrice()).boxed().collect(Collectors.toList());
    

VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  

References
    https://github.com/akraskovski/java-8-features

    Reverse a comparator in Java 8
        https://stackoverflow.com/questions/32995559/reverse-a-comparator-in-java-8#:~:text=You%20can%20use%20Comparator.,reversed()%20.
    Find maximum, minimum, sum and average of a list in Java 8
        https://stackoverflow.com/questions/25988707/find-maximum-minimum-sum-and-average-of-a-list-in-java-8/25988761
    Why can't mapToInt be used with collect(toList())?        
        https://stackoverflow.com/questions/50552258/why-cant-maptoint-be-used-with-collecttolist    
    How to find second highest salary in below array list using Java8 streams  
        https://stackoverflow.com/questions/64927047/how-to-find-second-highest-salary-in-below-array-list-using-java8-streams  
        Optional<Employee> emp = employeeList.stream()
            .sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).skip(1).findFirst();
            
```