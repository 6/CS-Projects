function [] = copyOnMod( x )

% show local variables
whos

% read values
disp(x(1:5, 1:5));

% write value
x(1:3,1:3) = 25;

% read value
disp(x(1:5, 1:5));

end