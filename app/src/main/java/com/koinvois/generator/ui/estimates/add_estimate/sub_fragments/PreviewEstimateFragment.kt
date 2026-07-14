package com.koinvois.generator.ui.estimates.add_estimate.sub_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koinvois.generator.R
import com.koinvois.generator.databinding.FragmentEstimatePreviewBinding
import com.koinvois.generator.ui.estimates.EstimatesMainViewModel
import com.koinvois.generator.utilities.extensions.visible
import com.koinvois.generator.utilities.utility
import java.lang.String

class PreviewEstimateFragment : Fragment() {

    private var binding: FragmentEstimatePreviewBinding? = null
    private val viewModel: EstimatesMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEstimatePreviewBinding.inflate(inflater, container, false)
        setUpToolbar()
        createHTML()

        return binding?.root
    }

    private fun setUpToolbar() {
        binding?.customToolbar?.btnBack?.visible()
        binding?.customToolbar?.txtToolbarTitle?.text = getString(R.string.label_preview_estimate)
        binding?.customToolbar?.btnBack?.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun createHTML() {

        val htmlContent = StringBuilder()
        htmlContent.append(
            String.format(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <style type=\"text/css\">      \n" +
                        "    body { font-family: 'Roboto Condensed Roboto';  margin: 0; padding: 0; box-sizing: border-box}\n" +
                        "        .main{ max-width: 1000px; min-width: 1000px;  margin: 10px auto }\n" +
                        "        .mainWrapper{ margin: 10px 40px}\n" +
                        "        .styled-table { border-collapse: collapse;  margin: 25px 0;  font-size: 11px;  font-family: 'PT Sans';" +
                        "font-weight: bold;width: 100%%;border: 2px solid rgb(73, 72, 72) }\n" +
                        "        .styled-table thead tr { background-color: rgb(73, 72, 72); color: #ffffff; text-align: left }\n" +
                        "        .styled-table th,\n" +
                        "        .styled-table td { padding: 12px 15px ; white-space: nowrap;}\n" +
                        "        .styled-table tbody tr { border-bottom: 1px solid #dddddd  }\n" +
                        "        .styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3  }\n" +
                        "        .logo1{ width: 50px;   height: 50px }\n" +
                        "        .invoicetag{ width: 100%%;    height: 40px; background-color: rgb(42, 68, 162)  }\n" +
                        "        .footertag{ width: 100%%;  height: 5px; background-color: coral  }\n" +
                        "        .invoiceWord{ font-size: 55px;   background-color: white; font-family: 'Times New Roman', Times, serif;  height: 40px;  width: 250px;  margin: 0;  margin-right: 4em; float: right; display: flex;    flex-direction: column;   align-items: center;     justify-content: center  }\n" +
                        "        .signword{ font-size: 55px;  background-color: white; width: 170px;  margin: 0; margin-right: 4em;   float: right;  display: flex; flex-direction: column;   align-items: center;  justify-content: center }\n" +
                        "        .subhead{ display: flex;      justify-content: space-between;   align-items: center   }\n" +
                        "        .wrapper2{   min-width: 150px;   }\n" +
                        "        .ivnum{ display: flex; align-items: center; justify-content: space-between    }\n" +
                        "        .total{ display: flex;  align-items: center; color: white;height: 50px; padding: 0px 15px; font-size: 24px; width: 323px; justify-content: space-between;background-color: rgb(42, 68, 162); margin-top: -130px; margin-bottom: 150px; position: relative;  right: 0; overflow: visible; margin-right: -40px }\n" +
                        "        .wrapper5{ display: flex;    flex-direction: row-reverse;   justify-content: space-between;  align-items: center      }\n" +
                        "        .footer-content{ display: flex; width: 50%%;   justify-content: space-between;       margin-top: 10px; font-size: " +
                        "12px;  font-weight: bold; align-items: center      ;       white-space: nowrap;\n }\n" +
                        "        .font-size{ font-size: 17px;      font-weight: bold }\n" +
                        "        .sign-line{ font-size: 17px;     font-weight: bold;     border-top: 4px solid darkgray;   padding-top: 5px}\n" +
                        "        .imgs-section{ margin-top: -50px;    margin-bottom: 50px}\n" +
                        "        .img-heading{ margin-bottom: -10px }\n" +
                        "        @media screen and (max-width: 640px)\n" +
                        "@media screen and (max-width: 490px){   .total{     font-size: 15px;     width: 160px;   } }\n" +
                        "  @media screen and (max-width: 450px){  .total{margin-top: -160px;  margin-right: 0;  } }\n" +
                        "        </style>\n" +
                        "    <title>Document</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<main class=\"main\">\n" +
                        "    <header>\n" +
                        "        <div class=\"mainWrapper\">\n" +
                        "            <div>\n" +
                        "                <img class=\"logo1 \"src=\"data:image/bmp;base64,${
                            utility.encodeToBase64(viewModel.businessUpdateModel?.businessLogo)
                                .toString()
                        }\"/>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"invoicetag\">\n" +
                        "            <p class=\"invoiceWord\">ESTIMATE</p>\n" +
                        "        </div>\n" +
                        "    </header>\n" +
                        "    <header>\n" +
                        "        <div class=\"mainWrapper\">\n" +
                        "            <h2 class=\"font-size\" style=\"margin:1px;\"> Estimate to:</h2>\n" +
                        "<div class=\"subhead\">" +
                        "<div >" +
                        "<article>" +
                        "                        <h3 class=\"font-size\">${viewModel.selectedClient?.clientName}</h3>\n" +
                        "                        <address>\n" +
                        "                            <p>\n" +
                        "                                ${viewModel.selectedClient?.clientAddress1} <br>\n" +
                        "                                ${viewModel.selectedClient?.clientAddress2} <br>\n" +
                        " ${viewModel.selectedClient?.clientAddress3} <br>\n" +
                        "                            </p>\n" +
                        "                        </address>\n" +
                        "                    </article>\n" +
                        "                </div>\n" +
                        "                <div style=\"margin: 0;\">\n" +
                        "                    <div class=\"ivnum\">\n" +
                        "                        <h3 class=\"font-size\" >Estimate# </h3>\n" +
                        "                        <p>${viewModel.estimateNumber}</p>\n" +
                        "                    </div>\n" +
                        "                    <div class=\"ivnum\">\n" +
                        "                        <h3 class=\"font-size\">Date </h3>\n" +
                        "                        <p>${viewModel.estimateDate}</p>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "    </header>\n" +
                        "    <mainBody>\n" +
                        "        <div class=\"mainWrapper\">\n" +
                        "            <table class=\"styled-table\">\n" +
                        "                <thead>\n" +
                        "                <tr>\n" +
                        "                    <th>SL.</th>\n" +
                        "                    <th>Item Description</th>\n" +
                        "                    <th>Price</th>\n" +
                        "                    <th>Quantity</th>\n" +
                        "                    <th>Total</th>\n" +
                        "                </tr>\n" +
                        "                </thead>\n"
            )
        )

        //if items list is not empty
        if (viewModel.selectedItemsList?.isNotEmpty() == true) {
            htmlContent.append("                <tbody>\n")
            viewModel.selectedItemsList?.withIndex()?.forEach {
                if (it.index % 2 == 0) {
                    htmlContent.append(
                        "                <tr>\n" +
                                "                    <td>${it.index}</td>\n" +
                                "                    <td>${it.value.estimateItemName}</td>\n" +
                                "                    <td>\$${it.value.estimateItemUnitCost}</td>\n" +
                                "                    <td>${it.value.estimateItemQuantity}</td>\n" +
                                "                    <td>\$${it.value.itemTotal}</td>\n" +
                                "                </tr>\n"
                    )
                } else {
                    htmlContent.append(
                        "                        <tr class=\"active-row\">\n" +
                                "                    <td>${it.index}</td>\n" +
                                "                    <td>${it.value.estimateItemName}</td>\n" +
                                "                    <td>\$${it.value.estimateItemUnitCost}</td>\n" +
                                "                    <td>${it.value.estimateItemQuantity}</td>\n" +
                                "                    <td>\$${it.value.itemTotal}</td>\n" +
                                "                </tr>\n"
                    )
                }
            }

            htmlContent.append(" </tbody>\n")
        }
//table closer
        htmlContent.append(
            "            </table>\n" +
                    "        </div>\n" +
                    "    </mainBody>\n"
        )

//bottom content
        htmlContent.append(
            "    <header>\n" +
                    "        <div class=\"mainWrapper\">\n" +
                    "            <h3 class=\"font-size\" style=\"margin-bottom: 10px; width: 500px; font:40px;\" > Thank you for your business</h3>\n" +
                    "            <div class=\"subhead\">\n" +
                    "                <div >\n" +
                    "                    <article>\n" +
                    "                        <h3 class=\"font-size\">Terms & Conditions</h3>\n" +
                    "                        <p>\n" +
                    //remove payment
                    "                           Make this payment by" +
                    "                        </p>\n" +
                    "                    </article>\n" +
                    "                </div>\n" +
                    "                <div style=\"margin: 0;\">\n" +
                    "                    <div class=\"wrapper2\">\n" +
                    "                        <div class=\"ivnum\">\n" +
                    "                            <p>sub Total: <p>\n" +
                    "                            <p>${viewModel.estimateSubTotal}</p>\n" +
                    "                        </div>\n" +
                    "                        <div class=\"ivnum\">\n" +
                    "                            <p>Tax </p>\n" +
                    "                            <p>${viewModel.taxRate}</p>\n" +
                    "                        </div>\n" +
                    "                        <div class=\"ivnum\">\n" +
                    "                            <p>Discount </p>\n" +
                    "                            <p>${viewModel.discountTotalAmount}</p>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "    </header>\n"
        )

        //payment information
        htmlContent.append(
            "    <header>\n" +
                    "        <div class=\"mainWrapper\">\n" +
                    "            <div class=\"subhead\">\n" +
                    "                <div >\n" +
                    "                    <article>\n" +
                    "                        <h3 class=\"font-size\" >Payment Info</h3>\n" +
                    "                        <p>\n" +
                    //remove payment due
                    "                            <br>\n" +
                    "                        </p>\n" +
                    "                    </article>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "            <div class=\"wrapper5\">\n" +
                    "                <div class=\"total\">\n" +
                    "                    <span>Total </span>\n" +
                    "                    <span >${viewModel.estimateTotal}</span>\n" +
                    "                </div>\n" +
                    "            </div>\n"
        )
//photos section
        if (viewModel.photosForEstimate?.isNotEmpty() == true) {
            htmlContent.append(
                "            <div class=\"imgs-section\">\n"
            )

            viewModel.photosForEstimate?.forEach {
                Log.e("photo", utility.encodeToBase64(it.estimatePhoto).toString())

                htmlContent.append(
                    "                <div >\n" +
                            "                    <article>\n" +
                            "                        <h4 class=\"img-heading\" >${it.estimatePhotoDescription}</h4>\n" +
                            "                        <p style=\"margin-bottom: 4px\">\n" +
                            "                            ${it.estimatePhotoAdditionalDetails}\n" +
                            "                        </p>\n" +
                            "                        <img src=\"data:image/bmp;base64,${
                                utility.encodeToBase64(it.estimatePhoto).toString()
                            } \" alt=\"user-image\" width=\"175px\" " +
                            "height=\"175px\" />\n" +
                            "                    </article>\n" +
                            "                </div>\n"
                )
            }

            htmlContent.append(
                "            </div>\n" +
                        "        </div>\n" +
                        "        </div>\n" +
                        "    </header>\n"
            )
        }

        //fotter
        htmlContent.append(
            "    <footerbody>\n" +
                    "        <div class=\"footer-line\">\n" +
                    "            <div class=\"footertag\">\n" +
                    "                <div class=\"signword\">\n" +
                    "                    <img src=\"data:image/bmp;base64,${
                        utility.encodeToBase64(viewModel.signatureObj?.signatureBitmap) ?: ""
                    }\" width=\"150\" height=\"150\" style=\"margin-top: -150px\" />\n" +
                    "                    <div class=\"sign-line\"  >Authorised Sign</div>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <div class=\"mainWrapper\">\n" +
                    "            <div class=\"footer-content\">\n" +
                    "                <span> ${viewModel.businessUpdateModel?.businessPhoneNumber} </span>\n" +
                    "                <span>|</span>\n" +
                    "                <span>${viewModel.businessUpdateModel?.businessAddress1} </span>\n" +
                    "                <span>|</span>\n" +
                    "                <span>${viewModel.businessUpdateModel?.businessWebsite} </span>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </footerbody>\n"

        )

        htmlContent.append(
            "</main>\n" +
                    "</body>\n" +
                    "</html>"
        )

        binding?.webView?.settings?.useWideViewPort = true
        binding?.webView?.settings?.loadWithOverviewMode = true
        binding?.webView?.settings?.useWideViewPort = true
        binding?.webView?.settings?.builtInZoomControls = true
        binding?.webView?.settings?.domStorageEnabled = true


        binding?.webView?.loadDataWithBaseURL(
            null,
            htmlContent.toString(),
            "text/html",
            "utf-8",
            null
        )

    }

}