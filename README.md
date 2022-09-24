# SETUP
The current working branch is P2P. I used VSCode to run my application.

# Run the Blockchain
The following outlines steps to run a new blockchain:
1. Run the runDaemon file - creates the blockchain & connects new nodes to network
2. Run the CustomBlockchainApplication file - creates a new node
3. Repeat Step 2 but change the server port in the application properties file (see below for details on port numbers)
4. Repeat step 3 as many times as you like (each time creates a new node)
5. Use postman (or equivalent) to query the node. (more details below)

Thats it! Your blockchain is up & running!

# Server Port Numbers
These are the port numbers which each node is running on your local machine. I only had one machine to test the network, so all nodes are run on a single machine. To test with multiple machines, you require the IP address to communicate between mahcines/nodes. This has not yet been implemented, so please mess around with the code & try for yourself!

To prevent any collisions between what you will see dubbed "runningPort" & "listeningPort", ensure that when you do Step 3 the port number is >7999 & <9000

# POSTMAN Queries (See BlockController)
The following can be used to query the node & interact with the blockchain. 

http://localhost:{serverPort}/
  
  chain/
  
    last - returns the last block in the chain
    
    full - returns the full chain
      
      
  transactions/
  
    new - creates a new transaction given the input data
      
    current - returns the transactions waiting to be mined into a block
      
      
  accounts/
  
    all - returns all accounts
      
    new - creates a new account
      
    IDs - returns the IDs of all the accounts
      
      
  mine - mines a new block

serverPort is the number you entered in step 2/3/4



# Blockchain Architecture
The architecture is currently being adapted from an account-based system stored on the blockchain to a wallet-based system which stores a lot more of the data locally.

The data security is also being improved, implementing public-private key cryptography to secure transactions & tokens

The updated blockchain shall be updated soon!
