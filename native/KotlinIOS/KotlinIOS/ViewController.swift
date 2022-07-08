import UIKit
import SharedCode

class TableViewCell: UITableViewCell {
    
    @IBOutlet var departureTime: UILabel!
    @IBOutlet var departureDate: UILabel!
    @IBOutlet var arrivalTime: UILabel!
    @IBOutlet var arrivalDate: UILabel!
    @IBOutlet var trainEmojiLabel: UILabel!
    @IBOutlet var priceLabel: UILabel!
    @IBOutlet var journeyTimeLabel: UILabel!
    
    override func awakeFromNib() {
        flipTrainEmoji()
    }
    
    func flipTrainEmoji(){
        trainEmojiLabel.transform = CGAffineTransform(scaleX: -1.5, y: 1.5);
    }
}

class ViewController: UIViewController, ApplicationContractView {

    @IBOutlet var tableView: UITableView!
    @IBOutlet var datePicker: UIDatePicker!
    @IBOutlet var adultCounter: UILabel!
    @IBOutlet var adultStepper: UIStepper!
    @IBOutlet var childCounter: UILabel!
    @IBOutlet var childStepper: UIStepper!
    @IBOutlet var originStationButton: UIButton!
    @IBOutlet var departureStationButton: UIButton!
    
    private let tableViewCell: String = "TableViewCell"

    let maxNumberTickets: Double = 8

    private var tableData: [DepartureInformation] = [DepartureInformation]()
    private var stations: [StationDetails] = [StationDetails]()
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var originStationCRS: String = ""
    private var destinationStationCRS: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        setUpTable()
        setUpTimePicker()
        setUpSteppers()
    }
    
    @IBAction func submitButtonTapped() {
        let dateTime = presenter.formatDateTimeInput(input: datePicker.date.description, format: "yyyy-MM-dd HH:mm:ss z")
        
        presenter.makeTrainSearch(originCrs: originStationCRS, destinationCrs: destinationStationCRS, dateTime: dateTime, adultCount: adultCounter.text!, childCount: childCounter.text!)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is SearchViewController {
            let vc = segue.destination as? SearchViewController
            vc?.stations = stations
            vc?.delegate = self
            vc?.button = (sender as! UIButton)
        }
    }
}

extension ViewController {
    func setStationNames(stationNames: Array<StationDetails>) {
        stations = stationNames
    }
}

extension ViewController: UITableViewDataSource, UITableViewDelegate {
    
    func setUpTable() {
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        registerTableViewCells()
    }
    
    func registerTableViewCells() {
        let trainCell = UINib(nibName: tableViewCell, bundle: nil)
        self.tableView.register(trainCell, forCellReuseIdentifier: tableViewCell)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) ->
        UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: tableViewCell, for: indexPath) as? TableViewCell{
            cell.departureTime.text = tableData[indexPath.row].departureDateTime.time
            cell.departureDate.text = tableData[indexPath.row].departureDateTime.date
            cell.arrivalTime.text = tableData[indexPath.row].arrivalDateTime.time
            cell.arrivalDate.text = tableData[indexPath.row].arrivalDateTime.date
            cell.priceLabel.text = tableData[indexPath.row].price
            cell.journeyTimeLabel.text = tableData[indexPath.row].journeyTime
            return cell
        }
        
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableData.count
    }
    
    func setTableData(data: [DepartureInformation]) {
        tableData = data
        self.tableView.reloadData()
    }
}

extension ViewController{
    
    func createAlert(alertTitle: String, alertMessage: String){
        let alert = UIAlertController(title: alertTitle, message: alertMessage, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Okay", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}

extension ViewController{
    
    func setUpTimePicker() {
        let dateString = datePicker.date.description
        
        let start = dateString.index(dateString.startIndex, offsetBy: 11)
        let end = dateString.index(dateString.startIndex, offsetBy: 18)
        let time = String(dateString[start...end])
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        let date = dateFormatter.date(from: dateString.prefix(11) + addHoursToTimeString(amount: 2, time: time))
        datePicker.date = date!
    }
    
    func addHoursToTimeString(amount: Int, time: String) -> String {
        let hour = (Int(time.prefix(2))! + amount) % 24
        return String(hour) + time.suffix(time.count - 2)
    }
}

extension ViewController{

    func setUpSteppers() {
        adultStepper.autorepeat = true
        adultStepper.maximumValue = maxNumberTickets

        childStepper.autorepeat = true
        childStepper.maximumValue = maxNumberTickets
    }

    @IBAction func adultValueChanged(_ sender: UIStepper) {
        adultCounter.text = Int(sender.value).description
    }


    @IBAction func childValueChanged(_ sender: UIStepper) {
        childCounter.text = Int(sender.value).description
    }
}

extension ViewController: SearchDelegate {
    func updateButtons(text: String, button: UIButton) {
        button.setTitle(text, for: .normal)
        switch button {
        case originStationButton:
            originStationCRS = text
        case departureStationButton:
            destinationStationCRS = text
        default:
            break
        }
    }
}
