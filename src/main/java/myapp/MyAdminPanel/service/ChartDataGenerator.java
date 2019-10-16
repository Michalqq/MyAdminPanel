package myapp.MyAdminPanel.service;

import myapp.MyAdminPanel.repository.MyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Component
public class ChartDataGenerator {

    @Autowired
    private ProfitCounter profitCounter;

    @Autowired
    private DateGenerator dateGenerator;

    @Autowired
    private MyItemRepository myItemRepository;

    @Autowired
    private CountItemSold countItemSold;


    public ModelAndView chartDataCreator(ModelAndView modelAndView, int quantityOfMonth, int quantityOfDay) {
        modelAndView.addObject("dataToChart", profitCounter.getProfitLastMonth(quantityOfMonth));
        modelAndView.addObject("monthNameToChart", dateGenerator.getNameOfLastMonth(quantityOfMonth));
        modelAndView.addObject("dataToItemSold", profitCounter.getSoldItemByLastMonth(quantityOfMonth));
        modelAndView.addObject("totalProfit", profitCounter.getProfitLastMonth(quantityOfMonth).stream().mapToInt(Integer::intValue).sum());
        modelAndView.addObject("totalItemSold", myItemRepository.countBySellPriceIsNotNull());
        this.getSoldSumByLastDays(modelAndView, quantityOfDay);
        return modelAndView;
    }

    public ModelAndView getSoldSumByLastDays(ModelAndView modelAndView, int quantityOfDay) {
        List<String> dataByDay = DateGenerator.getLastDate(quantityOfDay);
        List<Double> soldByDayList = countItemSold.getLastSoldSumData(quantityOfDay, dataByDay);
        Collections.reverse(soldByDayList);
        Collections.reverse(dataByDay);
        modelAndView.addObject("dataToEarningByDays", soldByDayList);
        modelAndView.addObject("labelToEarningByDays", dataByDay);
        modelAndView.addObject("totalEarningLastDays", soldByDayList.stream().mapToDouble(Double::intValue).sum());
        return modelAndView;
    }

}
