jQuery(function(){ 
  //call function with id what plot to draw
  $("#graph1").on('click',function(){ drawPlot(1); })
  $("#graph2").on('click',function(){ drawPlot(2); })
  $("#graph3").on('click',function(){ drawPlot(3); })
  
  $(window).resize(function() {
    drawPlot($('input[name=graph]:checked').val());
  });
});// end for jQuery



function drawPlot(graph_id){
  //clean plot wraper
  $('.plot_wraper').empty();
  // options
  var margin = {top: 10, right: 4, bottom: 110, left: 40},
      width = $('.plot_wraper').width() - margin.left - margin.right,
      height = $('.plot_wraper').height() - margin.top - margin.bottom;

  var formatPercent = d3.format(".%");

  var x = d3.scale.ordinal()
        .rangeRoundBands([0, width], .1);

  var y = d3.scale.linear()
        .range([height, 0]);

  var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom");

  var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left")
        .tickFormat(formatPercent);

  var svg = d3.select("div div").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
      .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  //select what graph to draw
  switch (graph_id) {
    case 1:
      drawPlotByState();
      break;
    case 2:
      drawPlotByDemographics();
      break;
    case 3:
      drawPlotByGeography();
      break;
    default:
      drawPlotByState();
  }

  function drawPlotByState() {
    // read file with data and draw
    d3.csv("plot.data.1.csv", function(error, data) {

      // Convert strings to numbers.
      data.forEach(function(d) {
        d.Obama = +d.Obama;
        d.Romney = +d.Romney;
        d.Others = +d.Others;
      });

      // Update the scale domains.
      x.domain(data.map(function(d) { return d.State; }));
      y.domain([0, 100]);

      svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis)
          .selectAll("text")  
            .style("text-anchor", "end")
            .attr("dx", "-.5em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
            });

      svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)

      svg.selectAll(".color3")
        .data(data)
      .enter().append("rect")
        .attr("class", "color3")
        .attr("x", function(d) { return x(d.State); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.Obama); }) // margin top
        .attr("height", function(d) { return height - y(d.Obama); }); // height of bar

      svg.selectAll(".color1")
        .data(data)
      .enter().append("rect")
        .attr("class", "color1")
        .attr("x", function(d) { return x(d.State); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.Romney+d.Obama); }) // margin top
        .attr("height", function(d) { return height - y(d.Romney); }); // height of bar

      svg.selectAll(".color2")
        .data(data)
      .enter().append("rect")
        .attr("class", "color2")
        .attr("x", function(d) { return x(d.State); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return 0; }) // margin top
        .attr("height", function(d) { return height - y(d.Others); }); // height of bar
    });
  }

  function drawPlotByDemographics() {
    // read file with data and draw
    d3.csv("plot.data.2.csv", function(error, data) {

      // Convert strings to numbers.
      data.forEach(function(d) {
        d.Obama = +d.Obama;
        d.Romney = +d.Romney;
        d.Others = +d.Others;
      });

      // Update the scale domains.
      x.domain(data.map(function(d) { return d.Age; }));
      y.domain([0, 100]);
    
      svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

      svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)
    
      svg.selectAll(".color3")
        .data(data)
      .enter().append("rect")
        .attr("class", "color3")
        .attr("x", function(d) { return x(d.Age); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.Obama); }) // margin top
        .attr("height", function(d) { return height - y(d.Obama); }); // height of bar
    
      svg.selectAll(".color1")
        .data(data)
      .enter().append("rect")
        .attr("class", "color1")
        .attr("x", function(d) { return x(d.Age); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.Romney+d.Obama); }) // margin top
        .attr("height", function(d) { return height - y(d.Romney); }); // height of bar
    
      svg.selectAll(".color2")
        .data(data)
      .enter().append("rect")
        .attr("class", "color2")
        .attr("x", function(d) { return x(d.Age); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return 0; }) // margin top
        .attr("height", function(d) { return height - y(d.Others); }); // height of bar*/

    });
  }

  function drawPlotByGeography() {
    // read file with data and draw
    d3.csv("plot.data.3.csv", function(error, data) {

      // Convert strings to numbers.
      data.forEach(function(d) {
        d.Obama = +d.Obama;
        d.Romney = +d.Romney;
      });

      // Update the scale domains.
      x.domain(data.map(function(d) { return d.State; }));
      y.domain([0, 100]);
    
      svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis)
          .selectAll("text")  
            .style("text-anchor", "end")
            .attr("dx", "-.5em")
            .attr("dy", ".15em")
            .attr("transform", function(d) {
                return "rotate(-65)" 
            });

      svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)

      svg.selectAll(".color3")
        .data(data)
      .enter().append("rect")
        .attr("class", "color3")
        .attr("x", function(d) { return x(d.State); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.Obama); }) // margin top
        .attr("height", function(d) { return height - y(d.Obama); }); // height of bar

      svg.selectAll(".color1")
        .data(data)
      .enter().append("rect")
        .attr("class", "color1")
        .attr("x", function(d) { return x(d.State); })
        .attr("width", x.rangeBand())
        .attr("y", function(d) { return y(d.Romney); }) // margin top
        .attr("height", function(d) { return height - y(d.Romney); }); // height of bar

    });
  }
}