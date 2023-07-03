# Conversion
A conversion program for a variety of conversion factors using a graph

This was inspired by this mock interview by Jane Street:
https://youtu.be/V8DGdPkBBxg

Graphs are a great way to implement rule sets that may be incomplete.  For example, we may
have rules that tell you how to convert feet to inches.  We may also have a rule 
to convert yards to feet.  We don't have a rule to convert yards to inches.  If we
construct a directed, weighted graph using the units as vertices and the conversion
rule with weight as an edge, we can convert yards to inches by seeing if a path from
yards to inches exists, and accumulating the conversion factors along the way.
