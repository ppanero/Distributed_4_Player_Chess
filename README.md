# Distributed_4_Player_Chess
4 player chess (all-to-all mode)

This is a distributed version of a 4 player game available in this repository: https://github.com/chanika/chess
In this case we can configure the network properties (addresses and ports) of the 4 players and also the failure detector
properties (ring structure, addresses and ports) so the ring can be checked for failed nodes. Both configurations
are in txt files available in the project.

The moves are sent to other users through encypted socket communication. Moves are serialized, encrypted and sent. Each palyer
has to receive other players move in order to be able to perform his own.

Future improvements: use RMI, use PKI or block ciphers...

NOTE: this was developed as project for Distributed Systems course at University of Oulu, so many things are no efficient and 
there may be bugs. The point was to get better understanding of concepts not the functionality itself.
