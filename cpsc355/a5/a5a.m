/*Tom Crowfoot/thcrowfo/10037477
Tutorial 01/Maryam
Assignment 5 CPSC 355*/				!
include(macro_defs.m)				!
		.align 4					!
		.section ".text"			!
star:	.byte "*"					!
		.align 4					!
perc:	.asciz "%c"					!	
begin_fn(find)						!
g:	set		ch,%o1					!moving &ch into %o1
	set		perc,%o0				!moving perc into %o0
	call	scanf					!calling scanf function
	nop								!delay slot
	sethi	%hi(ch),%o0				!loading ch value
	ldub	[%o0+%lo(ch)],%l0		!ch value is in %l0
	cmp		%l0," "					!checking if ch==' '
	be		g						!do loop if equal
	nop								!delay slot
	sethi	%hi(ch),%o0				!loading ch value
	ldub	[%o0+%lo(ch)],%l0		!ch value is in %l0
	cmp		%l0,-1					!checking if ch==EOF
	bne		h						!branch if not equal
	nop								!delay slot
	call	exit					!calling exit function
	nop								!delay slot
h:									!
end_fn(find)						!
local_var							!
var(op_s,1)							!
begin_fn(expression)				!expression subroutine
	call	term					!calling term subroutine
	nop								!delay slot
e:	sethi	%hi(ch),%o0				!loading ch value
	ldub	[%o0+%lo(ch)],%l0		!ch value is in %l0
	cmp		%l0,"+"					!checking if ch=='+'
	be		j						!if equal go into loop
	nop								!delay slot
	cmp		%l0,"-"					!checking if ch=='-'
	be		j						!if equal go into loop
	nop								!delay slot
	ba		f						!exit loop if not equal
	nop								!delay slot
j:	stb		%l0,[%fp+op_s]			!storing ch value in op
	call	find					!calling find subroutine
	nop								!delay slot
	call	term					!calling term subroutine
	nop								!delay slot
	ldub	[%fp+op_s],%o1		!op_s value is in %o1
	set		perc,%o0				!loading perc to print
	call	printf					!calling printf subroutine
	nop								!delay slot
	ba		e						!restart loop
	nop								!delay slot
f:									!
end_fn(expression)					!
begin_fn(term)						!term subroutine
	call	factor					!calling factor function
	nop								!delay slot
a:	sethi	%hi(ch),%o0				!loading ch value
	ldub	[%o0+%lo(ch)],%l0		!ch value is in %l0
	cmp		%l0,"*"					!checking if ch=="*"
	bne		b						!exit loop if not equal
	nop								!delay slot
	call	find					!calling find function
	nop								!delay slot
	call	factor					!calling factor function
	nop								!delay slot
	set		star,%o0				!moving "*" to call printf
	call	printf					!calling printf function
	nop								!delay slot
	ba		a						!restarting loop
	nop								!delay slot
b:									!
end_fn(term)						!
begin_fn(factor)					!factor subroutine
	sethi	%hi(ch),%o0				!loading ch value
	ldub	[%o0+%lo(ch)],%l0		!ch value is in %l0
	cmp		%l0,"("					!checking if ch=='('
	bne		c						!else if not equal
	nop								!delay slot
	call	find					!calling find function
	nop								!delay slot
	call	expression				!calling expression function
	nop								!delay slot
	ba		d						!branch over else
	nop								!delay slot
c:	sethi	%hi(ch),%o0				!loading ch value
	ldub	[%o0+%lo(ch)],%o1		!ch value is in %l0
	set		perc,%o0				!moving %c to print
	call	printf					!calling printf function
	nop								!delay slot
d:	call	find					!calling find function
	nop								!delay slot
end_fn(factor)						!
