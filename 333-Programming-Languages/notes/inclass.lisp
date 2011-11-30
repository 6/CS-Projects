;;;; Examples of recursion built in class.


;; generate a random list of n numbers
(defun randomList (size maxNum)
	(if ( <= size 0)
		'()
		(cons (random maxNum) (randomList (- size 1) maxNum)) ))

(print (randomList 10 100))


;; build a list of numbers in the range [a ... b-1]
(defun range (a b)
	(if (<= b a)
		'()
		(cons a (range (+ a 1) b)) ))

(print (range 5 12))


;; reverse a list
(defun rev (L)
	(if (eq L '())
		'()
		(append (rev (cdr L)) (list (car L)))))

(print (rev (range 5 12)))




;; Labels enable defining local functions.
;; A common pattern defines a recursive function
;; in a labels expression and invokes it from the
;; labels body.
;; This can be used to make functions tail-recursive.

;; reverse a list using tail recursion
(defun revtail (data)
	(labels ((revutil (data revdata)
			(if (eq data '())
				revdata
				(revutil (cdr data) (cons (car data) revdata)) )))
		(revutil data '()) ))

(print (revtail (range 25 32)))