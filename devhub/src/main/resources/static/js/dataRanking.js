const krTechStackNameArray = [];
const krTechStackTotalRecruitCntArray = [];
const krTechStackRecruitRateArray = [];
let krTechStackGreatTotalRecruitCnt = 0;

// 초기에 krTechStackList를 받아오고 차트 렌더링
function renderChart (radioValue) {
  $.ajax ({
    type: 'get',
    url: 'getAjaxKrTechStackList',
    data: {krPage: 10, techStackType: radioValue},
    success: function (krTechStackList) {
      const options = getChartOptions (krTechStackList);

      const chart = new ApexCharts (
        document.getElementById ('donut-chart'),
        options
      );
      chart.render ();

      // 이벤트 리스너 등록
      attachEventListeners (chart, radioValue);
    },
    error: function (error) {
      console.error ('Error in initial data request:', error);
    },
  });
}

function attachEventListeners (chart, radioValue) {
  const checkRadios = document.querySelectorAll (
    '#majorcoTechStack input[type="radio"]'
  );

  checkRadios.forEach (checkRadio => {
    checkRadio.addEventListener ('change', event =>
      handleCheckboxChange (event, chart)
    );
  });
}

function handleCheckboxChange (event, chart) {
  const checkRadio = event.target;
  $.ajax ({
    type: 'get',
    url: '/getAjaxKrTechStackList',
    data: {krPage: 500, techStackType: checkRadio.value},
    success: function (krTechStackList) {
      const newOptions = getChartOptions (krTechStackList);
      chart.updateOptions (newOptions);
    },
    error: function (error) {
      console.error ('Error in Ajax request:', error);
    },
  });
}
// 초기 차트 렌더링 실행

renderChart (0);

function getChartOptions (krTechStackList) {
  console.log ('radioValue : ' + krTechStackList[4].skill_name);
  console.log ('radioValue : ' + krTechStackList[4].total_recruit_cnt_ismajor);
  krTechStackNameArray.length = 0;
  krTechStackTotalRecruitCntArray.length = 0;
  krTechStackRecruitRateArray.length = 0;

  krTechStackList.sort((a, b) => b.total_recruit_cnt_ismajor - a.total_recruit_cnt_ismajor);
  krTechStackList.slice (0, 5).forEach (krTechStack => {
    krTechStackNameArray.push (krTechStack.skill_name);
    krTechStackTotalRecruitCntArray.push (
      krTechStack.total_recruit_cnt_ismajor
    );
    krTechStackGreatTotalRecruitCnt = +krTechStack.total_recruit_cnt_ismajor;
  });
  krTechStackList.slice (0, 5).forEach (krTechStack => {
    krTechStackRecruitRateArray.push (
      krTechStack.total_recruit_cnt_ismajor / krTechStackGreatTotalRecruitCnt
    );
  });

  return {
    series: krTechStackTotalRecruitCntArray,
    colors: [
      '#FF0000',
      '#FF7F00',
      '#FFFF00',
      '#ADFF2F',
      '#008000',
      '#0D98BA',
      '#0000FF',
      '#000080',
      '#7F00FF',
      '#871B4D',
    ],
    chart: {
      height: 320,
      width: '100%',
      type: 'donut',
    },
    stroke: {
      colors: ['transparent'],
      lineCap: '',
    },
    plotOptions: {
      pie: {
        donut: {
          labels: {
            show: true,
            name: {
              show: true,
              fontFamily: 'Inter, sans-serif',
              offsetY: 20,
            },
            total: {
              showAlways: true,
              show: true,
              label: '기술 스택',
              fontFamily: 'Inter, sans-serif',
              color: '#FFFFFF', // 글씨 색상을 하얗게 설정
              fontSize: '14px', // 글씨 크기를 조절
              formatter: function (w) {
                const sum = w.globals.seriesTotals.reduce ((a, b) => {
                  return a + b;
                }, 0);
                return '총 ' + sum + ' 개';
              },
            },
            value: {
              show: true,
              fontFamily: 'Inter, sans-serif',
              offsetY: -20,
              formatter: function (value) {
                return value + ' 개';
              },
            },
          },
          size: '80%',
        },
      },
    },
    grid: {
      padding: {
        top: -2,
      },
    },
    labels: krTechStackNameArray,
    dataLabels: {
      enabled: false,
    },
    legend: {
      position: 'bottom',
      fontFamily: 'Inter, sans-serif',
    },
    yaxis: {
      labels: {
        formatter: function (value) {
          return value + ' 개';
        },
      },
    },
    xaxis: {
      labels: {
        formatter: function (value) {
          return value + ' 개';
        },
      },
      axisTicks: {
        show: false,
      },
      axisBorder: {
        show: false,
      },
    },
  };
}










/////////////////////////////////////////////////////////////
const gblLangRateArray = [];
const gblLangNameArray = [];

function gblLangRenderInit () {
  $.ajax ({
    type: 'get',
    url: 'getAjaxGblLangRankingList',
    data: {gblPage: 10},
    success: function (axjaxGblLangList) {
      axjaxGblLangList.slice (0, 5).forEach (gblLang => {
        gblLangRateArray.push (gblLang.rate + ' % ');
        gblLangNameArray.push (gblLang.skill_name);
      });
      gblLangRenderChart ();
      console.log (gblLangRateArray);
    },
  });

  function gblLangRenderChart () {
    const options = {
      series: [
        {
          name: '세계적 관심도 지분율',
          colors: ['#7A92EC'],
          data: gblLangRateArray,
        },
      ],
      chart: {
        sparkline: {
          enabled: true,
        },
        type: 'bar',
        width: '100%',
        height: 400,
        toolbar: {
          show: false,
        },
      },
      fill: {
        opacity: 1,
      },
      plotOptions: {
        bar: {
          horizontal: true,

          columnWidth: '100%',
          borderRadiusApplication: 'end',
          borderRadius: 6,
          dataLabels: {
            position: 'top',
          },
        },
      },
      legend: {
        show: true,
        position: 'bottom',
      },
      dataLabels: {
        //bar끝에 숫자
        enabled: true,
        style: {
          fontSize: '20px', // 원하는 폰트 크기
          fontFamily: 'Arial, sans-serif', // 원하는 폰트 패밀리
          fontWeight: 'bold', // 원하는 폰트 두께
          fontColor: 'black', // 원하는 폰트 색상
        },
      },
      tooltip: {
        shared: true,
        intersect: false,
        formatter: function (value) {
          return '' + value;
        },
      },
      xaxis: {
        labels: {
          show: true,
          style: {
            fontFamily: 'Inter, sans-serif',
            cssClass: 'text-xs font-normal fill-gray-500 dark:fill-gray-400',
          },
          formatter: function (value) {
            return '   ' + value;
          },
        },
        categories: gblLangNameArray,
        axisTicks: {
          show: false,
        },
        axisBorder: {
          show: false,
        },
      },
      yaxis: {
        labels: {
          show: true,
          style: {
            fontFamily: 'Inter, sans-serif',
            cssClass: 'text-xs font-normal fill-gray-500 dark:fill-gray-400',
          },
        },
      },
      grid: {
        show: true,
        strokeDashArray: 4,
        padding: {
          left: 2,
          right: 2,
          top: -20,
        },
      },
      fill: {
        opacity: 1,
      },
    };

    if (
      document.getElementById ('bar-chart') &&
      typeof ApexCharts !== 'undefined'
    ) {
      const chart = new ApexCharts (
        document.getElementById ('bar-chart'),
        options
      );
      chart.render ();
    }
  }
}
gblLangRenderInit ();

$ (window).on ('pointermove mousewheel resize', function () {
  $ ('path[id^="SvgjsPath"][stroke-linecap="round"]').each (function (
    index,
    element
  ) {
    var colors = [
      'red',
      'orange',
      'green',
      'blue',
      'indigo',
      'Violet',
      'rgba(169, 237, 55, 0.5)',
      'rgba(143, 26, 35, 0.5)',
      'rgba(230, 179, 2, 0.5)',
    ];

    $ (element).css ('fill', colors[index]);
    
  });
  
$(".apexcharts-text").addClass(" text-red-500 ");

});

$ (window).on ('resize', function () {
  $ ('path[id^="SvgjsPath"][stroke-linecap="round"]').each (function (
    index,
    element
  ) {
    var colors = [
      'red',
      'orange',
      'green',
      'blue',
      'indigo',
      'Violet',
      'rgba(169, 237, 55, 0.5)',
      'rgba(143, 26, 35, 0.5)',
      'rgba(230, 179, 2, 0.5)',
    ];

    $ (element).css ('fill', colors[index]);
  });
});
