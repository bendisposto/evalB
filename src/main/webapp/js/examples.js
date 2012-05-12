example_list = {
	"Golumb Ruler" : "n=7 & length = 25 & \n\
			 a:1..n --> 0..length & \n\
			 !i.(i:2..n => a(i-1) < a(i)) & \n\
			 !(i1,j1,i2,j2).(( i1>0 & i2>0 & j1<=n & j2 <= n & \n\
			                   i1<j1 & i2<j2 & (i1,j1) /= (i2,j2) &  \n\
			                   i1<=i2 & (i1=i2 => j1<j2) \n\
			                 ) => (a(j1)-a(i1) /= a(j2)-a(i2)))",

	"Simple Expression" : "2 ** 5",
	"Simple Predicate" : "x > 2 ** 5"
}