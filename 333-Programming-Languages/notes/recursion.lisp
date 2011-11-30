;;; Simple Lisp examples

(defun square (x)
	(format t "square ~d~%" x)
	(* x x))

(defun factorial (x)
	(if (<= x 0)
		1
		(* x (factorial (- x 1)))))

(defun rev (data)
	(if (eq data '()) 
		'()
		(append (rev (cdr data)) (list (car data)))))
