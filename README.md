# Stock-correlations
This is a technical analysis program for stocks. If you want to invest in a share,
the program looks to see whether the market looks similar today as it did at the time when the share
had the desired price direction.

## Starting points
Because data science always takes a while, I have implemented different starting points.<br><br>
- parser.Main.main(String[] args)	// Downloads all data.
- parser.Test.main(String[] args)	// Tests with example values
- analysis.MakeData.main(String[] args) // Converts the data from the graph into numbers. And assigns scores to the stocks at different intervals.
