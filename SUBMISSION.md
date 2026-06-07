# Moodle Short Answer

The hardest technical issue was separating the start marker bit 16 from the movement bits while still storing each room as one integer. I solved it by masking the current room value before checking directions, so only bits 1, 2, 4, and 8 decide whether a move is allowed. I also added tests for start detection, one-way movement, boundary blocking, and finishing when the player reaches the zero-value room.
